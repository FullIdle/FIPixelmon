package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import com.pixelmonmod.pixelmon.api.events.ShopkeeperEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.items.helpers.ItemHelper;
import io.netty.buffer.ByteBuf;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ShopKeeperPacket implements IMessage {
   EnumBuySell buySell;
   String itemID;
   int amount;
   int shopKeeperID;

   public ShopKeeperPacket() {
   }

   public ShopKeeperPacket(EnumBuySell buySell, int shopkeeperID, String itemID, int amount) {
      this.shopKeeperID = shopkeeperID;
      this.itemID = itemID;
      this.amount = amount;
      this.buySell = buySell;
   }

   public void fromBytes(ByteBuf buf) {
      if (buf.readBoolean()) {
         this.buySell = EnumBuySell.Buy;
      } else {
         this.buySell = EnumBuySell.Sell;
      }

      this.shopKeeperID = buf.readInt();
      this.itemID = ByteBufUtils.readUTF8String(buf);
      this.amount = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.buySell == EnumBuySell.Buy);
      buf.writeInt(this.shopKeeperID);
      ByteBufUtils.writeUTF8String(buf, this.itemID);
      buf.writeInt(this.amount);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ShopKeeperPacket message, MessageContext ctx) {
         EntityPlayerMP p = ctx.getServerHandler().field_147369_b;
         Optional npcOptional = EntityNPC.locateNPCServer(p.field_70170_p, message.shopKeeperID, NPCShopkeeper.class);
         IPixelmonBankAccount account = (IPixelmonBankAccount)Pixelmon.moneyManager.getBankAccount(p).orElse((Object)null);
         if (npcOptional.isPresent() && message.amount > 0 && account != null) {
            NPCShopkeeper npc = (NPCShopkeeper)npcOptional.get();
            ArrayList itemList;
            Iterator var8;
            ShopItemWithVariation shopItem;
            ItemStack sStack;
            ItemStack buyStack;
            int initialAmount;
            if (message.buySell == EnumBuySell.Buy) {
               itemList = npc.getItemList();
               var8 = itemList.iterator();

               while(var8.hasNext()) {
                  shopItem = (ShopItemWithVariation)var8.next();
                  if (shopItem.getBaseShopItem().id.equals(message.itemID)) {
                     if (message.amount > PixelmonConfig.getShopMaxStackSize(shopItem.getItemStack())) {
                        return;
                     }

                     if (account.getMoney() >= shopItem.getBuyCost() * message.amount) {
                        if (shopItem.getBuyCost() * message.amount < 0) {
                           return;
                        }

                        BigInteger total = BigInteger.valueOf((long)message.amount).multiply(BigInteger.valueOf((long)shopItem.getBuyCost()));
                        if (total.compareTo(BigInteger.valueOf(2147483647L)) >= 0) {
                           Pixelmon.LOGGER.error("Integer overflow whilst purchasing item from " + npc.func_70005_c_() + " player: " + p.func_70005_c_());
                           return;
                        }

                        sStack = shopItem.getItemStack();
                        buyStack = sStack.func_77946_l();
                        initialAmount = message.amount;
                        buyStack.func_190920_e(message.amount);
                        if (Pixelmon.EVENT_BUS.post(new ShopkeeperEvent.Purchase(p, npc, buyStack, EnumBuySell.Buy))) {
                           return;
                        }

                        if (p.func_191521_c(buyStack)) {
                           Item buyItem = buyStack.func_77973_b();
                           if (buyItem instanceof ItemPokeball && ((ItemPokeball)buyItem).type == EnumPokeballs.PokeBall && message.amount >= 10) {
                              ItemStack premierBall = new ItemStack(PixelmonItemsPokeballs.premierBall, 1);
                              p.func_191521_c(premierBall);
                           }

                           account.changeMoney(-shopItem.getBuyCost() * message.amount);
                           this.updateTransaction(p, npc);
                           return;
                        }

                        if (initialAmount > buyStack.func_190916_E()) {
                           int actualAmount = initialAmount - buyStack.func_190916_E();
                           account.changeMoney(-shopItem.getBuyCost() * actualAmount);
                           this.updateTransaction(p, npc);
                           return;
                        }
                     }
                  }
               }

            } else {
               itemList = npc.getSellList(p);
               var8 = itemList.iterator();

               while(true) {
                  int count;
                  do {
                     do {
                        if (!var8.hasNext()) {
                           return;
                        }

                        shopItem = (ShopItemWithVariation)var8.next();
                     } while(!shopItem.getBaseShopItem().id.equals(message.itemID));

                     count = 0;
                     sStack = shopItem.getItemStack();

                     for(int i = 0; i < p.field_71071_by.field_70462_a.size(); ++i) {
                        ItemStack item = (ItemStack)p.field_71071_by.field_70462_a.get(i);
                        if (this.areItemsEqual(item, sStack)) {
                           count += item.func_190916_E();
                        }
                     }

                     buyStack = sStack.func_77946_l();
                     buyStack.func_190920_e(message.amount);
                     if (Pixelmon.EVENT_BUS.post(new ShopkeeperEvent.Sell(p, npc, EnumBuySell.Sell, buyStack))) {
                        return;
                     }
                  } while(count < message.amount);

                  initialAmount = shopItem.getSellCost();
                  count = message.amount;
                  BigInteger total = BigInteger.valueOf((long)message.amount).multiply(BigInteger.valueOf((long)shopItem.getSellCost()));
                  if (total.compareTo(BigInteger.valueOf(2147483647L)) >= 0) {
                     Pixelmon.LOGGER.error("Integer overflow whilst selling item to " + npc.func_70005_c_() + " player: " + p.func_70005_c_());
                     return;
                  }

                  for(int i = 0; i < p.field_71071_by.field_70462_a.size(); ++i) {
                     ItemStack item = (ItemStack)p.field_71071_by.field_70462_a.get(i);
                     if (this.areItemsEqual(item, sStack)) {
                        if (item.func_190916_E() >= count) {
                           item.func_190920_e(item.func_190916_E() - count);
                           count = 0;
                        } else {
                           count -= item.func_190916_E();
                           item.func_190920_e(0);
                        }

                        if (item.func_190916_E() == 0) {
                           p.field_71071_by.field_70462_a.set(i, ItemStack.field_190927_a);
                        }
                     }

                     if (count <= 0) {
                        break;
                     }
                  }

                  account.changeMoney(initialAmount * message.amount);
                  this.updateTransaction(p, npc);
               }
            }
         }
      }

      private void updateTransaction(EntityPlayerMP p, NPCShopkeeper npc) {
         p.field_71069_bz.func_75142_b();
         npc.sendItemsToPlayer(p);
      }

      private boolean areItemsEqual(ItemStack item1, ItemStack item2) {
         return !item1.func_190926_b() && ItemHelper.isItemStackEqual(item1, item2);
      }
   }
}

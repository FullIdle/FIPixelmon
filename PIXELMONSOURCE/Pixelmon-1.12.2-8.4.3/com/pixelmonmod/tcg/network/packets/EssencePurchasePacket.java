package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.set.CardSet;
import com.pixelmonmod.tcg.api.registries.CardSetRegistry;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.helper.EssenceHelper;
import com.pixelmonmod.tcg.network.PacketHandler;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EssencePurchasePacket implements IMessage {
   private int set;

   public EssencePurchasePacket() {
   }

   public EssencePurchasePacket(int set) {
      this.set = set;
   }

   public void fromBytes(ByteBuf buf) {
      this.set = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.set);
   }

   public static int getLowestEssenceValue(EntityPlayer player) {
      int curLowest = -1;
      Energy[] var2 = Energy.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Energy e = var2[var4];
         if (e != Energy.RAINBOW) {
            int c = EssenceHelper.getPlayerEssenceFromEnergy((EntityPlayerMP)player, e);
            if (curLowest == -1 || c < curLowest) {
               curLowest = c;
            }
         }
      }

      return curLowest;
   }

   public static void deductFromAll(EntityPlayer player, int deduct) {
      Energy[] var2 = Energy.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Energy e = var2[var4];
         int c = EssenceHelper.getPlayerEssenceFromEnergy((EntityPlayerMP)player, e);
         EssenceHelper.setPlayerEssenceFromEnergy((EntityPlayerMP)player, e, c - deduct);
      }

   }

   public static int getAmount(EntityPlayer player, Item i) {
      NonNullList items = player.field_71071_by.field_70462_a;
      int has = 0;
      Iterator var4 = items.iterator();

      while(var4.hasNext()) {
         ItemStack item = (ItemStack)var4.next();
         if (item != ItemStack.field_190927_a && item.func_77973_b() == i && item.func_190916_E() > 0) {
            has += item.func_190916_E();
         }
      }

      return has;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(EssencePurchasePacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         player.func_71121_q().func_152344_a(() -> {
            int dropped = 0;
            int cardset = -1;
            if (message.set == 0) {
               if (EssencePurchasePacket.getLowestEssenceValue(player) >= TCGConfig.getInstance().randomPackEssenceCost) {
                  EssencePurchasePacket.deductFromAll(player, TCGConfig.getInstance().randomPackEssenceCost);
                  cardset = CardSetRegistry.getRandom().getID();
               }
            } else {
               CardSet set = CardSetRegistry.get(message.set);
               if (set != null && EssencePurchasePacket.getLowestEssenceValue(player) >= TCGConfig.getInstance().specificPackEssenceCost) {
                  EssencePurchasePacket.deductFromAll(player, TCGConfig.getInstance().specificPackEssenceCost);
                  cardset = message.set;
               }
            }

            if (cardset > -1) {
               ItemStack itemStack = new ItemStack(TCG.itemPack);
               NBTTagCompound tag = new NBTTagCompound();
               tag.func_74768_a("SetID", cardset);
               tag.func_74776_a("Weight", 0.5F);
               itemStack.func_77982_d(tag);
               if (!player.func_191521_c(itemStack)) {
                  player.func_71019_a(itemStack, true);
                  ChatHandler.sendFormattedChat(player, TextFormatting.RED, "pixelmon.drops.fullinventory", dropped + " " + I18n.func_74838_a("tcg.pack.name"));
               }

               player.field_71069_bz.func_75142_b();
            }

            PacketHandler.net.sendTo(new EssenceSyncPacket(player), player);
         });
         return null;
      }
   }
}

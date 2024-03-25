package com.pixelmonmod.pixelmon.comm.packetHandlers.vendingMachine;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import com.pixelmonmod.pixelmon.blocks.machines.BlockVendingMachine;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class VendingMachinePacket implements IMessage {
   String itemID;
   BlockPos pos;

   public VendingMachinePacket() {
   }

   public VendingMachinePacket(BlockPos pos, String itemName) {
      this.pos = pos;
      this.itemID = itemName;
   }

   public void fromBytes(ByteBuf buf) {
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      this.pos = new BlockPos(x, y, z);
      this.itemID = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.pos.func_177958_n());
      buf.writeInt(this.pos.func_177956_o());
      buf.writeInt(this.pos.func_177952_p());
      ByteBufUtils.writeUTF8String(buf, this.itemID);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(VendingMachinePacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (BlockHelper.validateReach(player, message.pos)) {
            IBlockState state = player.field_70170_p.func_180495_p(message.pos);
            IPixelmonBankAccount account = (IPixelmonBankAccount)Pixelmon.moneyManager.getBankAccount(player).orElse((Object)null);
            if (state.func_177230_c() instanceof BlockVendingMachine && account != null) {
               ArrayList itemList = ((BlockVendingMachine)state.func_177230_c()).getShop().getItems();
               Iterator var7 = itemList.iterator();

               while(var7.hasNext()) {
                  ShopItemWithVariation s = (ShopItemWithVariation)var7.next();
                  if (s.getBaseShopItem().id.equals(message.itemID) && account.getMoney() >= s.getBuyCost()) {
                     ItemStack item = s.getItemStack();
                     ItemStack buyStack = item.func_77946_l();
                     buyStack.func_190920_e(1);
                     if (player.func_191521_c(buyStack)) {
                        account.changeMoney(-s.getBuyCost());
                        player.field_71069_bz.func_75142_b();
                        player.func_71053_j();
                     }
                  }
               }
            }

         }
      }
   }
}

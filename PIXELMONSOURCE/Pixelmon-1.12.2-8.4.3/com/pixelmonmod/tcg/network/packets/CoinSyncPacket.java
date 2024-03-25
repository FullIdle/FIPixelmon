package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.TCG;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CoinSyncPacket implements IMessage {
   private int slot;
   private String name;

   public CoinSyncPacket() {
   }

   public CoinSyncPacket(int slot, String name) {
      this.slot = slot;
      this.name = name;
   }

   public void fromBytes(ByteBuf buf) {
      this.slot = buf.readInt();
      this.name = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.slot);
      ByteBufUtils.writeUTF8String(buf, this.name);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(CoinSyncPacket message, MessageContext ctx) {
         Minecraft mc = Minecraft.func_71410_x();
         EntityPlayer e = mc.field_71439_g;
         if (e != null) {
            ItemStack i = e.field_71071_by.func_70301_a(message.slot);
            if (i != ItemStack.field_190927_a && i.func_77973_b() == TCG.itemCoin) {
               if (i.func_77978_p() == null) {
                  i.func_77982_d(new NBTTagCompound());
               }

               NBTTagCompound tag = i.func_77978_p();
               tag.func_74778_a("CoinID", message.name);
            }
         }

         return null;
      }
   }
}

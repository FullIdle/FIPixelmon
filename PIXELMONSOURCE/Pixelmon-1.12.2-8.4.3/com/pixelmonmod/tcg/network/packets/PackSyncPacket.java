package com.pixelmonmod.tcg.network.packets;

import com.pixelmonmod.tcg.TCG;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PackSyncPacket implements IMessage {
   private int slot;
   private int setID;
   private float weight;

   public PackSyncPacket() {
   }

   public PackSyncPacket(int slot, int setID, float weight) {
      this.slot = slot;
      this.setID = setID;
      this.weight = weight;
   }

   public void fromBytes(ByteBuf buf) {
      this.slot = buf.readInt();
      this.setID = buf.readInt();
      this.weight = buf.readFloat();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.slot);
      buf.writeInt(this.setID);
      buf.writeFloat(this.weight);
   }

   public static class Handler implements IMessageHandler {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PackSyncPacket message, MessageContext ctx) {
         if (ctx.side == Side.CLIENT) {
            Minecraft mc = Minecraft.func_71410_x();
            EntityPlayer e = mc.field_71439_g;
            if (e != null) {
               ItemStack i = e.field_71071_by.func_70301_a(message.slot);
               if (i != ItemStack.field_190927_a && i.func_77973_b() == TCG.itemCard) {
                  if (i.func_77978_p() == null) {
                     i.func_77982_d(new NBTTagCompound());
                  }

                  NBTTagCompound tag = i.func_77978_p();
                  tag.func_74768_a("SetID", message.setID);
                  tag.func_74776_a("Weight", message.weight);
               }
            }
         }

         return null;
      }
   }
}

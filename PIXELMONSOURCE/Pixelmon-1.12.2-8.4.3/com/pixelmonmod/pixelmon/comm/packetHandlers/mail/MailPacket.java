package com.pixelmonmod.pixelmon.comm.packetHandlers.mail;

import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMail;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MailPacket implements IMessage {
   private Boolean sealed;
   private String contents;

   public MailPacket() {
   }

   public MailPacket(Boolean sealed, String contents) {
      this.sealed = sealed;
      this.contents = contents;
   }

   public void fromBytes(ByteBuf buf) {
      this.sealed = buf.readBoolean();
      this.contents = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.sealed);
      ByteBufUtils.writeUTF8String(buf, this.contents);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(MailPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         ItemStack letter = player.func_184614_ca();
         if (!letter.func_190926_b() && letter.func_77973_b() instanceof ItemMail) {
            if (letter.func_77952_i() == 1) {
               return;
            }

            if (message.contents.split("\n").length > 14) {
               return;
            }

            String[] var5 = message.contents.split("\n");
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String line = var5[var7];
               if (line.length() > 200) {
                  return;
               }
            }

            NBTTagCompound nbtData = new NBTTagCompound();
            nbtData.func_74757_a("editable", !message.sealed);
            nbtData.func_74778_a("author", player.func_70005_c_());
            nbtData.func_74778_a("contents", message.contents);
            if (letter.func_190916_E() > 1) {
               letter.func_190918_g(1);
               ItemStack stack = new ItemStack(letter.func_77973_b(), 1);
               if (message.sealed) {
                  stack.func_77964_b(1);
               }

               stack.func_77982_d(nbtData);
               player.func_191521_c(stack);
            } else {
               if (message.sealed) {
                  letter.func_77964_b(1);
               }

               letter.func_77982_d(nbtData);
            }

            player.field_71069_bz.func_75142_b();
         }

      }
   }
}

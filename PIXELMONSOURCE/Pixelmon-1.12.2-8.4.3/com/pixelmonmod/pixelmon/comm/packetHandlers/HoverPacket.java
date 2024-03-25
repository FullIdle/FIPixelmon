package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6Moves;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HoverPacket implements IMessage {
   int id;

   public HoverPacket() {
   }

   public HoverPacket(EntityPixelmon pixelmon) {
      this.id = pixelmon.func_145782_y();
   }

   public void fromBytes(ByteBuf buffer) {
      this.id = buffer.readInt();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.id);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(final HoverPacket message, final MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(new Runnable() {
            public void run() {
               Entity entity = ctx.getServerHandler().field_147369_b.field_70170_p.func_73045_a(message.id);
               if (entity instanceof Entity6Moves) {
                  ((Entity6Moves)entity).toggleHover();
               }

            }
         });
         return null;
      }
   }
}

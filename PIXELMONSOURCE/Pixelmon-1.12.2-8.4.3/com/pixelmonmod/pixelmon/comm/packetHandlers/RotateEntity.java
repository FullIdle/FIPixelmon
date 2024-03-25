package com.pixelmonmod.pixelmon.comm.packetHandlers;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RotateEntity implements IMessage {
   int entityID;
   float yaw;
   float pitch;

   public RotateEntity() {
   }

   public RotateEntity(int entityID, float yaw, float pitch) {
      this.entityID = entityID;
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public void fromBytes(ByteBuf buffer) {
      this.entityID = buffer.readInt();
      this.yaw = buffer.readFloat();
      this.pitch = buffer.readFloat();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.entityID);
      buffer.writeFloat(this.yaw);
      buffer.writeFloat(this.pitch);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(RotateEntity message, MessageContext ctx) {
         Entity entity = Minecraft.func_71410_x().field_71441_e.func_73045_a(message.entityID);
         if (entity != null) {
            entity.func_70080_a(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, message.yaw, message.pitch);
            entity.field_70177_z = message.yaw;
            entity.func_70091_d(MoverType.SELF, 0.1, 0.1, 0.1);
         }

         return null;
      }
   }
}

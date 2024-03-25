package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6Moves;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdatePixelmonMovementPacket implements IMessage {
   int id;
   int takeOffTicks;
   float lastSpeed;
   float speed;
   float rotationPitch;
   float rotationYaw;
   float strafe;
   float strafeUp;
   double posX;
   double posY;
   double posZ;
   Vec3d targetPosition;
   boolean hasTargetPos = false;

   public UpdatePixelmonMovementPacket() {
   }

   public UpdatePixelmonMovementPacket(Entity6Moves pixelmon) {
      this.id = pixelmon.func_145782_y();
      this.speed = pixelmon.moveMultiplier;
      this.strafe = pixelmon.field_70702_br;
      this.strafeUp = pixelmon.strafeUpDown;
      this.targetPosition = pixelmon.getTargetPosition();
      this.hasTargetPos = this.targetPosition != null;
      this.takeOffTicks = pixelmon.takeOffTicks;
      this.posX = pixelmon.field_70165_t;
      this.posY = pixelmon.field_70163_u;
      this.posZ = pixelmon.field_70161_v;
      this.rotationPitch = pixelmon.field_70125_A;
      this.rotationYaw = pixelmon.field_70177_z;
      this.lastSpeed = pixelmon.lastSpeed;
   }

   public void fromBytes(ByteBuf buffer) {
      this.id = buffer.readInt();
      this.speed = buffer.readFloat();
      this.strafe = buffer.readFloat();
      this.strafeUp = buffer.readFloat();
      this.takeOffTicks = buffer.readInt();
      this.posX = buffer.readDouble();
      this.posY = buffer.readDouble();
      this.posZ = buffer.readDouble();
      this.rotationPitch = buffer.readFloat();
      this.rotationYaw = buffer.readFloat();
      this.lastSpeed = buffer.readFloat();
      if (buffer.readBoolean()) {
         this.hasTargetPos = true;
         this.targetPosition = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.id);
      buffer.writeFloat(this.speed);
      buffer.writeFloat(this.strafe);
      buffer.writeFloat(this.strafeUp);
      buffer.writeInt(this.takeOffTicks);
      buffer.writeDouble(this.posX);
      buffer.writeDouble(this.posY);
      buffer.writeDouble(this.posZ);
      buffer.writeFloat(this.rotationPitch);
      buffer.writeFloat(this.rotationYaw);
      buffer.writeFloat(this.lastSpeed);
      buffer.writeBoolean(this.hasTargetPos);
      if (this.hasTargetPos) {
         buffer.writeDouble(this.targetPosition.field_72450_a);
         buffer.writeDouble(this.targetPosition.field_72448_b);
         buffer.writeDouble(this.targetPosition.field_72449_c);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UpdatePixelmonMovementPacket message, MessageContext ctx) {
         Entity entity = Minecraft.func_71410_x().field_71441_e.func_73045_a(message.id);
         if (entity instanceof Entity6Moves) {
            Entity6Moves pixelmon = (Entity6Moves)entity;
            if (pixelmon.func_184207_aI() && pixelmon.func_184179_bs() == Minecraft.func_71410_x().field_71439_g) {
               return null;
            }

            if (message.hasTargetPos) {
               pixelmon.setTarget(message.targetPosition);
            }

            pixelmon.moveMultiplier = message.speed;
            pixelmon.field_70702_br = message.strafe;
            pixelmon.strafeUpDown = message.strafeUp;
            pixelmon.takeOffTicks = message.takeOffTicks;
            pixelmon.field_70125_A = message.rotationPitch;
            if (Math.abs(pixelmon.field_70165_t - message.posX) > 1.0 || Math.abs(pixelmon.field_70163_u - message.posY) > 1.0 || Math.abs(pixelmon.field_70161_v - message.posZ) > 1.0) {
               pixelmon.func_70107_b(message.posX, message.posY, message.posZ);
            }

            pixelmon.field_70125_A = message.rotationPitch;
            pixelmon.field_70177_z = message.rotationYaw;
            pixelmon.lastSpeed = message.lastSpeed;
            if (!pixelmon.lastFlyingState) {
               pixelmon.takeOff();
               pixelmon.lastFlyingState = true;
            }
         }

         return null;
      }
   }
}

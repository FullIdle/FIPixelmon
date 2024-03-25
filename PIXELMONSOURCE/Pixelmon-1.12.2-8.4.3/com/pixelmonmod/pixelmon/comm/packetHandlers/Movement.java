package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.entities.bikes.EntityBike;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity4Interactions;
import com.pixelmonmod.pixelmon.enums.EnumMovement;
import com.pixelmonmod.pixelmon.tools.IMovementHandler;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Movement implements IMessage {
   public static HashMap movementList = new HashMap();
   static IMovementHandler handler;
   EnumMovementType type;
   EnumMovement[] movement;

   public static void registerMovementHandler(IMovementHandler mHandler) {
      handler = mHandler;
   }

   public Movement() {
   }

   public Movement(EnumMovement[] movement, EnumMovementType type) {
      this.movement = movement;
      this.type = type;
   }

   public void fromBytes(ByteBuf buffer) {
      this.type = EnumMovementType.getFromOrdinal(buffer.readByte());
      int numMovements = buffer.readShort();
      this.movement = new EnumMovement[numMovements];

      for(int i = 0; i < numMovements; ++i) {
         this.movement[i] = EnumMovement.getMovement(buffer.readShort());
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.type.ordinal());
      buffer.writeShort(this.movement.length);
      EnumMovement[] var2 = this.movement;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumMovement aMovement = var2[var4];
         buffer.writeShort(aMovement.ordinal());
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(Movement message, MessageContext ctx) {
         if (message.type == EnumMovementType.Riding) {
            EntityPlayer player = ctx.getServerHandler().field_147369_b;
            int var6;
            if (player.func_184187_bx() instanceof Entity4Interactions) {
               Entity4Interactions mount = (Entity4Interactions)player.func_184187_bx();
               EnumMovement[] var5 = message.movement;
               var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  EnumMovement movementType = var5[var7];
                  if (movementType == EnumMovement.Jump) {
                     ++mount.ridingPlayerVertical;
                  } else if (movementType == EnumMovement.Descend) {
                     --mount.ridingPlayerVertical;
                  }
               }
            } else if (player.func_184187_bx() instanceof EntityBike && player.func_184187_bx().field_70122_E) {
               EnumMovement[] var9 = message.movement;
               int var10 = var9.length;

               for(var6 = 0; var6 < var10; ++var6) {
                  EnumMovement movementType = var9[var6];
                  if (movementType == EnumMovement.Jump) {
                     ((EntityBike)player.func_184187_bx()).jumping = true;
                  }
               }
            }
         } else if (Movement.handler != null) {
            Movement.handler.handleMovement(ctx.getServerHandler().field_147369_b, message.movement);
         }

      }
   }
}

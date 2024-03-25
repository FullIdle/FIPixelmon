package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6Moves;
import com.pixelmonmod.pixelmon.enums.EnumKeybinds;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MovementPacket implements IMessage {
   ArrayList movementList;

   public MovementPacket() {
   }

   public MovementPacket(ArrayList movements) {
      this.movementList = movements;
   }

   public void fromBytes(ByteBuf buffer) {
      int numMovements = buffer.readShort();
      this.movementList = new ArrayList();

      for(int i = 0; i < numMovements; ++i) {
         this.movementList.add(EnumKeybinds.getMovement(buffer.readShort()));
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.movementList.size());
      Iterator var2 = this.movementList.iterator();

      while(var2.hasNext()) {
         EnumKeybinds aMovement = (EnumKeybinds)var2.next();
         buffer.writeShort(aMovement.ordinal());
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(final MovementPacket message, final MessageContext ctx) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(new Runnable() {
            public void run() {
               EntityPlayer player = ctx.getServerHandler().field_147369_b;
               if (player.func_184187_bx() instanceof Entity6Moves) {
                  ((Entity6Moves)player.func_184187_bx()).handleMovement(message.movementList);
               }

            }
         });
         return null;
      }
   }
}

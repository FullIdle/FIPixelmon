package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DeleteNPC implements IMessage {
   int trainerId;

   public DeleteNPC() {
   }

   public DeleteNPC(int trainerId) {
      this.trainerId = trainerId;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.trainerId);
   }

   public void fromBytes(ByteBuf buffer) {
      this.trainerId = buffer.readInt();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(DeleteNPC message, MessageContext ctx) {
         EntityPlayerMP p = ctx.getServerHandler().field_147369_b;
         if (!ItemNPCEditor.checkPermission(p)) {
            return null;
         } else {
            Optional npc = EntityNPC.locateNPCServer(p.field_70170_p, message.trainerId, EntityNPC.class);
            if (npc.isPresent()) {
               ((EntityNPC)npc.get()).unloadEntity();
            }

            return null;
         }
      }
   }
}

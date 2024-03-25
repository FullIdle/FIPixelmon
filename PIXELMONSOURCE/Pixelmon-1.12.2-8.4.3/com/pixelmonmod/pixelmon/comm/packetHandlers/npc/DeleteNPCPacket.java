package com.pixelmonmod.pixelmon.comm.packetHandlers.npc;

import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.items.ItemNPCEditor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DeleteNPCPacket implements IMessage {
   int entityId;

   public DeleteNPCPacket() {
   }

   public DeleteNPCPacket(int entityId) {
      this.entityId = entityId;
   }

   public void fromBytes(ByteBuf buf) {
      this.entityId = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.entityId);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(DeleteNPCPacket message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (ItemNPCEditor.checkPermission(player)) {
            Entity entity = player.field_70170_p.func_73045_a(message.entityId);
            if (entity instanceof EntityNPC) {
               entity.func_70106_y();
            }

         }
      }
   }
}

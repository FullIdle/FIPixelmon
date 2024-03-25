package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateTerrain implements IMessage {
   private StatusType terrain;

   public UpdateTerrain() {
   }

   public UpdateTerrain(StatusType terrain) {
      this.terrain = terrain;
   }

   public void fromBytes(ByteBuf buf) {
      if (buf.readBoolean()) {
         this.terrain = StatusType.values()[buf.readInt()];
      } else {
         this.terrain = null;
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.terrain != null);
      if (this.terrain != null) {
         buf.writeInt(this.terrain.ordinal());
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdateTerrain message, MessageContext ctx) {
         ClientProxy.battleManager.terrain = message.terrain;
      }
   }
}

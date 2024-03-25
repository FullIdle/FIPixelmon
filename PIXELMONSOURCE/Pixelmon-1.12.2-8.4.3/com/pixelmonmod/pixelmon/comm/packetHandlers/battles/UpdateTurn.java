package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateTurn implements IMessage {
   private int battleTurn;

   public UpdateTurn() {
   }

   public UpdateTurn(int battleTurn) {
      this.battleTurn = battleTurn;
   }

   public void fromBytes(ByteBuf buf) {
      this.battleTurn = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.battleTurn);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdateTurn message, MessageContext ctx) {
         ClientProxy.battleManager.battleTurn = message.battleTurn;
      }
   }
}

package com.pixelmonmod.pixelmon.comm.packetHandlers.trading;

import com.pixelmonmod.pixelmon.client.gui.ClientTradingManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TradeReady implements IMessage {
   boolean ready;

   public TradeReady() {
   }

   public TradeReady(boolean ready) {
      this.ready = ready;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.ready);
   }

   public void fromBytes(ByteBuf buffer) {
      this.ready = buffer.readBoolean();
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(TradeReady message, MessageContext ctx) {
         ClientTradingManager.player2Ready = message.ready;
         return null;
      }
   }
}

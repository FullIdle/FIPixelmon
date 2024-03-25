package com.pixelmonmod.pixelmon.comm.packetHandlers.trading;

import com.pixelmonmod.pixelmon.client.gui.ClientTradingManager;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetSelectedStats implements IMessage {
   PixelmonStatsData stats;

   public SetSelectedStats() {
   }

   public SetSelectedStats(PixelmonStatsData stats) {
      this.stats = stats;
   }

   public void toBytes(ByteBuf buffer) {
      this.stats.writePacketData(buffer);
   }

   public void fromBytes(ByteBuf buffer) {
      this.stats = new PixelmonStatsData();
      this.stats.readPacketData(buffer);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetSelectedStats message, MessageContext ctx) {
         ClientTradingManager.selectedStats = message.stats;
         return null;
      }
   }
}

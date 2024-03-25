package com.pixelmonmod.pixelmon.comm.packetHandlers.trading;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.ClientTradingManager;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetTradeTarget implements IMessage {
   Pokemon data;
   PixelmonStatsData stats;
   boolean clear = false;

   public SetTradeTarget() {
   }

   public SetTradeTarget(Pokemon data, PixelmonStatsData stats) {
      this.data = data;
      this.stats = stats;
   }

   public SetTradeTarget(boolean clear) {
      this.clear = clear;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeBoolean(this.clear);
      if (!this.clear) {
         this.data.writeToByteBuffer(buffer, EnumUpdateType.CLIENT);
         this.stats.writePacketData(buffer);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.clear = buffer.readBoolean();
      if (!this.clear) {
         this.data = Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buffer, EnumUpdateType.CLIENT);
         this.stats = new PixelmonStatsData();
         this.stats.readPacketData(buffer);
      }

   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetTradeTarget message, MessageContext ctx) {
         if (message.clear) {
            ClientTradingManager.tradeTarget = null;
            ClientTradingManager.tradeTargetStats = null;
            ClientTradingManager.player1Ready = false;
            ClientTradingManager.player2Ready = false;
         } else {
            ClientTradingManager.tradeTarget = message.data;
            ClientTradingManager.tradeTargetStats = message.stats;
            ClientTradingManager.player1Ready = false;
            ClientTradingManager.player2Ready = false;
         }

         return null;
      }
   }
}

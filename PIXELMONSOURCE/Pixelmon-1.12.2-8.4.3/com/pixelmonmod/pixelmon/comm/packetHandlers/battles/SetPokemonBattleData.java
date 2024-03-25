package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetPokemonBattleData implements IMessage {
   PixelmonInGui[] data;
   boolean isOpponent;

   public SetPokemonBattleData() {
   }

   public SetPokemonBattleData(PixelmonInGui[] controlledPokemon) {
      this(controlledPokemon, true);
   }

   public SetPokemonBattleData(PixelmonInGui[] controlledPokemon, boolean isOpponent) {
      this.data = controlledPokemon;
      this.isOpponent = isOpponent;
   }

   public void toBytes(ByteBuf buffer) {
      int numPokemon = this.data.length;
      PixelmonInGui[] var3 = this.data;
      int var4 = var3.length;

      int var5;
      PixelmonInGui d;
      for(var5 = 0; var5 < var4; ++var5) {
         d = var3[var5];
         if (d == null) {
            --numPokemon;
         }
      }

      buffer.writeShort(numPokemon);
      var3 = this.data;
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         d = var3[var5];
         if (d != null) {
            d.encodeInto(buffer);
         }
      }

      buffer.writeBoolean(this.isOpponent);
   }

   public void fromBytes(ByteBuf buffer) {
      this.data = new PixelmonInGui[buffer.readShort()];

      for(int i = 0; i < this.data.length; ++i) {
         this.data[i] = new PixelmonInGui();
         this.data[i].decodeFrom(buffer);
      }

      this.isOpponent = buffer.readBoolean();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SetPokemonBattleData message, MessageContext ctx) {
         if (message.isOpponent) {
            ClientProxy.battleManager.setOpponents(message.data);
         } else {
            ClientProxy.battleManager.setTeamPokemon(message.data);
         }

      }
   }
}

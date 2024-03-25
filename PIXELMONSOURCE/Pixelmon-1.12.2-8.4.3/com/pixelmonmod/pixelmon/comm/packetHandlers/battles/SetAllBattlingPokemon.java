package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetAllBattlingPokemon implements IMessage {
   PixelmonInGui[] allPokemon;

   public SetAllBattlingPokemon() {
   }

   public SetAllBattlingPokemon(PixelmonInGui[] allPokemon) {
      this.allPokemon = allPokemon;
   }

   public void toBytes(ByteBuf buffer) {
      int numPokemon = this.allPokemon.length;
      PixelmonInGui[] var3 = this.allPokemon;
      int var4 = var3.length;

      int var5;
      PixelmonInGui d;
      for(var5 = 0; var5 < var4; ++var5) {
         d = var3[var5];
         if (d == null) {
            --numPokemon;
         }
      }

      buffer.writeByte(numPokemon);
      var3 = this.allPokemon;
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         d = var3[var5];
         if (d != null) {
            d.encodeInto(buffer);
         }
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.allPokemon = new PixelmonInGui[buffer.readByte()];

      for(int i = 0; i < this.allPokemon.length; ++i) {
         this.allPokemon[i] = new PixelmonInGui();
         this.allPokemon[i].decodeFrom(buffer);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SetAllBattlingPokemon message, MessageContext ctx) {
         ClientProxy.battleManager.setFullTeamData(message.allPokemon);
      }
   }
}

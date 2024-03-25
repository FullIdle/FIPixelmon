package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetBattlingPokemon implements IMessage {
   UUID[] pokemon;

   public SetBattlingPokemon() {
   }

   public SetBattlingPokemon(ArrayList arrayList) {
      int numPokemon = arrayList.size();
      this.pokemon = new UUID[numPokemon];

      for(int i = 0; i < numPokemon; ++i) {
         PixelmonWrapper pw = (PixelmonWrapper)arrayList.get(i);
         if (pw != null) {
            this.pokemon[i] = pw.getPokemonUUID();
         }
      }

   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.pokemon.length);
      UUID[] var2 = this.pokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         UUID uuid = var2[var4];
         PixelmonMethods.toBytesUUID(buffer, uuid);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      int size = buffer.readShort();
      this.pokemon = new UUID[size];

      for(int i = 0; i < size; ++i) {
         this.pokemon[i] = new UUID(buffer.readLong(), buffer.readLong());
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SetBattlingPokemon message, MessageContext ctx) {
         ClientProxy.battleManager.setTeamPokemon(message.pokemon);
      }
   }
}

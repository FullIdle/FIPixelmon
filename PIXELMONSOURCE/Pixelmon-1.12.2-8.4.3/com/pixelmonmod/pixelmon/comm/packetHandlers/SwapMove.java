package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SwapMove implements IMessage {
   StoragePosition position;
   UUID pokemonUUID;
   int selected;
   int clicked;

   public SwapMove() {
   }

   public SwapMove(StoragePosition position, UUID pokemonUUID, int selected, int clicked) {
      this.position = position;
      this.pokemonUUID = pokemonUUID;
      this.selected = selected;
      this.clicked = clicked;
   }

   public void fromBytes(ByteBuf buffer) {
      this.position = StoragePosition.decode(buffer);
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.selected = buffer.readByte();
      this.clicked = buffer.readByte();
   }

   public void toBytes(ByteBuf buffer) {
      this.position.encode(buffer);
      buffer.writeLong(this.pokemonUUID.getMostSignificantBits());
      buffer.writeLong(this.pokemonUUID.getLeastSignificantBits());
      buffer.writeByte(this.selected);
      buffer.writeByte(this.clicked);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SwapMove message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         if (message.selected > -1 && message.selected < 4) {
            if (message.clicked > -1 && message.clicked < 4) {
               Object storage;
               if (message.position.box == -1) {
                  storage = Pixelmon.storageManager.getParty(player);
               } else {
                  storage = Pixelmon.storageManager.getPCForPlayer(player);
               }

               Pokemon pokemon = ((PokemonStorage)storage).get(message.position);
               if (pokemon != null && pokemon.getUUID().equals(message.pokemonUUID)) {
                  pokemon.getMoveset().swap(message.selected, message.clicked);
               }
            }
         }
      }
   }
}

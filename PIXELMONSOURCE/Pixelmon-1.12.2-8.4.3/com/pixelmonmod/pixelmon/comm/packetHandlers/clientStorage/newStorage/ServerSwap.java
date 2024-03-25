package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerSwap implements IMessage {
   private StoragePosition position1;
   private UUID pokemon1;
   private StoragePosition position2;
   private UUID pokemon2;

   public ServerSwap() {
   }

   public ServerSwap(StoragePosition position1, Pokemon pokemon1, StoragePosition position2, Pokemon pokemon2) {
      this.position1 = position1;
      this.pokemon1 = pokemon1 == null ? null : pokemon1.getUUID();
      this.position2 = position2;
      this.pokemon2 = pokemon2 == null ? null : pokemon2.getUUID();
   }

   public void toBytes(ByteBuf buf) {
      this.position1.encode(buf);
      buf.writeBoolean(this.pokemon1 != null);
      if (this.pokemon1 != null) {
         buf.writeLong(this.pokemon1.getMostSignificantBits());
         buf.writeLong(this.pokemon1.getLeastSignificantBits());
      }

      this.position2.encode(buf);
      buf.writeBoolean(this.pokemon2 != null);
      if (this.pokemon2 != null) {
         buf.writeLong(this.pokemon2.getMostSignificantBits());
         buf.writeLong(this.pokemon2.getLeastSignificantBits());
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.position1 = StoragePosition.decode(buf);
      if (buf.readBoolean()) {
         this.pokemon1 = new UUID(buf.readLong(), buf.readLong());
      }

      this.position2 = StoragePosition.decode(buf);
      if (buf.readBoolean()) {
         this.pokemon2 = new UUID(buf.readLong(), buf.readLong());
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ServerSwap message, MessageContext ctx) {
         PokemonStorage storage1 = Pixelmon.storageManager.getStorage(ctx.getServerHandler().field_147369_b, message.position1);
         PokemonStorage storage2 = Pixelmon.storageManager.getStorage(ctx.getServerHandler().field_147369_b, message.position2);
         if (storage1.validate(message.position1, message.pokemon1) && storage2.validate(message.position2, message.pokemon2)) {
            Pokemon fromPokemon = storage1.get(message.position1);
            Pokemon toPokemon = storage2.get(message.position2);
            if (fromPokemon != null && fromPokemon.isInRanch()) {
               return;
            }

            if (toPokemon != null && toPokemon.isInRanch()) {
               return;
            }

            if (fromPokemon != null) {
               fromPokemon.ifEntityExists(EntityPixelmon::retrieve);
            }

            if (toPokemon != null) {
               toPokemon.ifEntityExists(EntityPixelmon::retrieve);
            }

            storage2.transfer(storage1, message.position1, message.position2);
         } else {
            storage1.notifyListener(ctx.getServerHandler().field_147369_b, message.position1, storage1.get(message.position1));
            storage2.notifyListener(ctx.getServerHandler().field_147369_b, message.position2, storage1.get(message.position2));
         }

      }
   }
}

package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.DeleteType;
import com.pixelmonmod.pixelmon.api.events.PixelmonDeletedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerTrash implements IMessage {
   private StoragePosition position;
   private UUID pokemon;

   public ServerTrash() {
   }

   public ServerTrash(StoragePosition position, Pokemon pokemon) {
      this.position = position;
      this.pokemon = pokemon == null ? null : pokemon.getUUID();
   }

   public void toBytes(ByteBuf buf) {
      this.position.encode(buf);
      buf.writeLong(this.pokemon.getMostSignificantBits());
      buf.writeLong(this.pokemon.getLeastSignificantBits());
   }

   public void fromBytes(ByteBuf buf) {
      this.position = StoragePosition.decode(buf);
      this.pokemon = new UUID(buf.readLong(), buf.readLong());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ServerTrash message, MessageContext ctx) {
         PokemonStorage storage = Pixelmon.storageManager.getStorage(ctx.getServerHandler().field_147369_b, message.position);
         if (!storage.validate(message.position, message.pokemon) || message.position.box == -1 && storage.countPokemon() <= 1 && !storage.get(message.position).isEgg()) {
            storage.notifyListener(ctx.getServerHandler().field_147369_b, message.position, storage.get(message.position));
         } else {
            Pokemon pokemon = storage.get(message.position);
            pokemon.ifEntityExists(EntityPixelmon::retrieve);
            Pixelmon.EVENT_BUS.post(new PixelmonDeletedEvent(ctx.getServerHandler().field_147369_b, pokemon, DeleteType.PC));
            storage.set(message.position, (Pokemon)null);
         }

      }
   }
}

package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StopStartLevelling implements IMessage {
   StoragePosition position;
   UUID pokemonUUID;

   public StopStartLevelling() {
   }

   public StopStartLevelling(StoragePosition position, UUID pokemonUUID) {
      this.position = position;
      this.pokemonUUID = pokemonUUID;
   }

   public void fromBytes(ByteBuf buffer) {
      this.position = StoragePosition.decode(buffer);
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
   }

   public void toBytes(ByteBuf buffer) {
      this.position.encode(buffer);
      buffer.writeLong(this.pokemonUUID.getMostSignificantBits());
      buffer.writeLong(this.pokemonUUID.getLeastSignificantBits());
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(StopStartLevelling message, MessageContext ctx) {
         EntityPlayerMP player = ctx.getServerHandler().field_147369_b;
         Pokemon pokemon = Pixelmon.storageManager.getPokemon(player, message.position);
         if (pokemon != null && pokemon.getUUID().equals(message.pokemonUUID)) {
            pokemon.setDoesLevel(!pokemon.doesLevel());
         }
      }
   }
}

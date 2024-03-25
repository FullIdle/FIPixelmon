package com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class UpdateEditedPokemon implements IMessage {
   public Pokemon data;

   protected UpdateEditedPokemon() {
   }

   protected UpdateEditedPokemon(Pokemon data) {
      this.data = data;
   }

   public void toBytes(ByteBuf buffer) {
      this.data.writeToByteBuffer(buffer, EnumUpdateType.ALL);
   }

   public void fromBytes(ByteBuf buffer) {
      this.data = Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buffer, EnumUpdateType.ALL);
   }

   public static void updatePokemon(UpdateEditedPokemon message, EntityPlayerMP player, PartyStorage storage) {
      int pos = storage.find(message.data.getUUID()) != null ? storage.getPosition(message.data).order : -1;
      message.data.heal();
      if (pos != -1 || storage.countAll() != 6) {
         if (pos == -1) {
            storage.add(message.data);
            pos = storage.getPosition(message.data).order;
         } else {
            storage.set(pos, message.data);
         }

         Pixelmon.network.sendTo(new SetImportPokemonID(pos, message.data.getUUID()), player);
      }
   }
}

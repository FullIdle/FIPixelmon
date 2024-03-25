package com.pixelmonmod.pixelmon.api.events.storage;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ChangeStorageEvent extends Event {
   public final PokemonStorage oldStorage;
   public final PokemonStorage newStorage;
   public final StoragePosition oldPosition;
   public final StoragePosition newPosition;
   public final Pokemon pokemon;

   public ChangeStorageEvent(PokemonStorage oldStorage, StoragePosition oldPosition, PokemonStorage newStorage, StoragePosition newPosition, Pokemon pokemon) {
      this.oldStorage = oldStorage;
      this.oldPosition = oldPosition;
      this.newStorage = newStorage;
      this.newPosition = newPosition;
      this.pokemon = pokemon;
   }
}

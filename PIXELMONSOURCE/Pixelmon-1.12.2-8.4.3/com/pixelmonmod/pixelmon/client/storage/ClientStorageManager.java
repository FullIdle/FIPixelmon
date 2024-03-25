package com.pixelmonmod.pixelmon.client.storage;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;

public class ClientStorageManager {
   public static PlayerPartyStorage party = new PlayerPartyStorage(new UUID(0L, 0L), false);
   public static HashMap pcs;
   public static PCStorage openPC;
   public static ClientPokedex pokedex = new ClientPokedex();

   public static void load() {
      party = new PlayerPartyStorage(Minecraft.func_71410_x().func_110432_I().func_148256_e().getId(), false);
      pcs = new HashMap();
      pokedex = new ClientPokedex();
   }

   public static Tuple getNext(PokemonStorage storage, StoragePosition position) {
      int maxSlots = storage instanceof PartyStorage ? 6 : 30;
      StoragePosition testPosition = new StoragePosition(position.box, 0);

      for(int slot = (position.order + 1) % maxSlots; slot != position.order; slot = (slot + 1) % maxSlots) {
         testPosition.order = slot;
         Pokemon pokemon = storage.get(testPosition);
         if (pokemon != null) {
            return new Tuple(pokemon, testPosition);
         }
      }

      return new Tuple(storage.get(position), position);
   }

   public static Tuple getPrevious(PokemonStorage storage, StoragePosition position) {
      int maxSlots = storage instanceof PartyStorage ? 6 : 30;
      StoragePosition testPosition = new StoragePosition(position.box, 0);

      for(int slot = position.order - 1; slot != position.order; --slot) {
         if (slot < 0) {
            slot = maxSlots - 1;
         }

         testPosition.order = slot;
         Pokemon pokemon = storage.get(testPosition);
         if (pokemon != null) {
            return new Tuple(pokemon, testPosition);
         }
      }

      return new Tuple(storage.get(position), position);
   }

   public static PokemonStorage getStorage(UUID storageUUID, StoragePosition position) {
      return (PokemonStorage)(position.box == -1 ? party : (PokemonStorage)pcs.get(storageUUID));
   }
}

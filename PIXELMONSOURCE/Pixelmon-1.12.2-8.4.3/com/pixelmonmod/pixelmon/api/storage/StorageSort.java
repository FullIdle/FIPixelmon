package com.pixelmonmod.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public abstract class StorageSort implements Comparator {
   public static StorageSort SPECIES = new StorageSort() {
      public int compare(Pokemon p1, Pokemon p2) {
         return Integer.compare(p1.getSpecies().getNationalPokedexInteger(), p2.getSpecies().getNationalPokedexInteger());
      }
   };
   public static StorageSort LEVEL = new StorageSort() {
      public int compare(Pokemon p1, Pokemon p2) {
         return Integer.compare(p1.getLevel(), p2.getLevel());
      }
   };

   public void apply(PokemonStorage storage, boolean ascending) {
      ArrayList startPositions = new ArrayList();
      ArrayList unsorted = new ArrayList();
      HashMap beforePositions = new HashMap();
      new HashMap();
      Pokemon[] allWithNulls = storage.getAll();
      ArrayList all = new ArrayList();
      Pokemon[] var9 = allWithNulls;
      int var10 = allWithNulls.length;

      int sign;
      for(sign = 0; sign < var10; ++sign) {
         Pokemon pokemon = var9[sign];
         if (pokemon != null) {
            StoragePosition position = storage.getPosition(pokemon);
            startPositions.add(position);
            unsorted.add(pokemon);
            all.add(pokemon);
            beforePositions.put(pokemon, position);
         }
      }

      if (all.size() > 1) {
         sign = ascending ? 1 : -1;
         boolean clearPass = true;

         Pokemon pokemon1;
         int i;
         do {
            clearPass = true;

            for(i = 0; i < all.size() - 1; ++i) {
               pokemon1 = (Pokemon)all.get(i);
               Pokemon pokemon2 = (Pokemon)all.get(i + 1);
               int compare = this.compare(pokemon1, pokemon2) * sign;
               if (compare < 0) {
                  all.set(i, pokemon2);
                  all.set(i + 1, pokemon1);
                  clearPass = false;
               }
            }
         } while(!clearPass);

         for(i = 0; i < all.size(); ++i) {
            pokemon1 = (Pokemon)all.get(i);
            storage.set((StoragePosition)startPositions.get(i), pokemon1);
         }

      }
   }
}

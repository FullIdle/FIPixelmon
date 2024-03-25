package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class MagnetPull extends AbilityBase {
   public boolean stopsSwitching(PixelmonWrapper user, PixelmonWrapper opponent) {
      return opponent.hasType(EnumType.Steel);
   }

   public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
      if (!(spawnInfo instanceof SpawnInfoPokemon)) {
         return 1.0F;
      } else {
         SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;
         if (spawnInfoPokemon.getPokemonSpec() != null && spawnInfoPokemon.getPokemonSpec().name != null) {
            EnumSpecies species = spawnInfoPokemon.getSpecies();
            int form = spawnInfoPokemon.getPokemonSpec().form == null ? -1 : spawnInfoPokemon.getPokemonSpec().form;
            return species.getBaseStats(species.getFormEnum(form)).getTypeList().contains(EnumType.Steel) ? 5.0F : 1.0F;
         } else {
            return 1.0F;
         }
      }
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class StormDrain extends Redirect {
   public StormDrain() {
      super(EnumType.Water, "pixelmon.abilities.stormdrain", "pixelmon.abilities.stormdrainredirect");
   }

   public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
      if (!(spawnInfo instanceof SpawnInfoPokemon)) {
         return 1.0F;
      } else {
         SpawnInfoPokemon spawnInfoPokemon = (SpawnInfoPokemon)spawnInfo;
         if (spawnInfoPokemon.getPokemonSpec() != null && spawnInfoPokemon.getPokemonSpec().name != null) {
            EnumSpecies species = spawnInfoPokemon.getSpecies();
            int form = spawnInfoPokemon.getPokemonSpec().form == null ? -1 : spawnInfoPokemon.getPokemonSpec().form;
            return species.getBaseStats(species.getFormEnum(form)).getTypeList().contains(EnumType.Water) ? 5.0F : 1.0F;
         } else {
            return 1.0F;
         }
      }
   }
}

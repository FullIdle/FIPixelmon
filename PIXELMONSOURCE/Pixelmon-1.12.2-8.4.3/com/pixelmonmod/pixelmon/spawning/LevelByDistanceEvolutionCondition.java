package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.ISpawnerCondition;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class LevelByDistanceEvolutionCondition implements ISpawnerCondition {
   public boolean fits(AbstractSpawner spawner, SpawnInfo spawnInfo, SpawnLocation spawnLocation) {
      if (!(spawnInfo instanceof SpawnInfoPokemon)) {
         return true;
      } else {
         SpawnInfoPokemon spawn = (SpawnInfoPokemon)spawnInfo;
         PokemonSpec spec = spawn.getPokemonSpec();
         EnumSpecies species = spawn.getSpecies();
         if (spec != null && spec.name != null && species != null) {
            BaseStats bs = species.getBaseStats(species.getFormEnum(spec.form == null ? 0 : spec.form));
            int levelBase = SpawnActionPokemon.getLevelBasedOnDistance(spawnLocation);
            int min = Math.max(levelBase - 5, 1);
            int max = Math.min(levelBase + 5, PixelmonConfig.maxLevelByDistance);
            if (min > bs.maxLevel || max < bs.minLevel) {
               return false;
            }
         }

         return true;
      }
   }
}

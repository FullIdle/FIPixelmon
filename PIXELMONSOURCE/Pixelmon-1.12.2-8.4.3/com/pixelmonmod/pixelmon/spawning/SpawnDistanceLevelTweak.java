package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.ISpawningTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.util.math.MathHelper;

public class SpawnDistanceLevelTweak implements ISpawningTweak {
   public void doTweak(AbstractSpawner spawner, SpawnAction action) {
      if (action instanceof SpawnActionPokemon) {
         SpawnActionPokemon spawnActionPokemon = (SpawnActionPokemon)action;
         EntityPixelmon pokemon = (EntityPixelmon)spawnActionPokemon.getOrCreateEntity();
         if (spawnActionPokemon.baseSpec == null || spawnActionPokemon.baseSpec.level == null) {
            int levelBase = SpawnActionPokemon.getLevelBasedOnDistance(action.spawnLocation);
            int min = Math.max(levelBase - 5, 1);
            int max = Math.min(levelBase + 5, PixelmonConfig.maxLevelByDistance);
            min = MathHelper.func_76125_a(min, pokemon.getBaseStats().minLevel, pokemon.getBaseStats().maxLevel);
            max = MathHelper.func_76125_a(max, min, pokemon.getBaseStats().maxLevel);
            if (max < min) {
               max = min;
            }

            pokemon.getLvl().setLevel(RandomHelper.getRandomNumberBetween(min, max));
            pokemon.func_70606_j((float)pokemon.getPokemonData().getHealth());
         }

      }
   }
}

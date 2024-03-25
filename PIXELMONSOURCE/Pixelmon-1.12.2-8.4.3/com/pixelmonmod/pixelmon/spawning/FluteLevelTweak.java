package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.ISpawningTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.api.storage.TransientData;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;

public class FluteLevelTweak implements ISpawningTweak {
   public void doTweak(AbstractSpawner spawner, SpawnAction action) {
      if (action instanceof SpawnActionPokemon) {
         SpawnActionPokemon spawnActionPokemon = (SpawnActionPokemon)action;
         EntityPixelmon pokemon = (EntityPixelmon)spawnActionPokemon.getOrCreateEntity();
         SpawnActionPokemon actionPokemon = (SpawnActionPokemon)action;
         if (actionPokemon.baseSpec.level == null && action.spawnInfo instanceof SpawnInfoPokemon) {
            if (!pokemon.isBossPokemon()) {
               int newLevel = this.getTweakedLevel(spawner, actionPokemon, actionPokemon.usingSpec.level);
               pokemon.getPokemonData().setLevel(newLevel);
               pokemon.func_70606_j((float)pokemon.getPokemonData().getHealth());
            }
         }
      }
   }

   public int getTweakedLevel(AbstractSpawner spawner, SpawnAction action, int level) {
      PlayerPartyStorage storage;
      if (spawner instanceof PlayerTrackingSpawner) {
         storage = ((PlayerTrackingSpawner)spawner).playerStorage;
      } else {
         storage = Pixelmon.storageManager.getParty((EntityPlayerMP)action.spawnLocation.cause);
      }

      if (storage == null) {
         return level;
      } else {
         TransientData td = storage.transientData;
         long time = System.currentTimeMillis();
         if (td.isBlackFluteActive(time)) {
            return MathHelper.func_76125_a(level + RandomHelper.rand.nextInt(PixelmonConfig.lureFluteLevelModifier), 1, PixelmonConfig.maxLevel);
         } else {
            return td.isWhiteFluteActive(time) ? MathHelper.func_76125_a(level - RandomHelper.rand.nextInt(PixelmonConfig.lureFluteLevelModifier), 1, PixelmonConfig.maxLevel) : level;
         }
      }
   }
}

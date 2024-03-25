package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.ISpawnerCondition;
import com.pixelmonmod.pixelmon.api.spawning.ISpawningTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs.trainers.SpawnActionNPCTrainer;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs.trainers.SpawnInfoNPCTrainer;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.api.storage.TransientData;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;

public class PlayerBasedLevels implements ISpawningTweak, ISpawnerCondition {
   public void doTweak(AbstractSpawner spawner, SpawnAction action) {
      if (action instanceof SpawnActionPokemon) {
         SpawnActionPokemon actionPokemon = (SpawnActionPokemon)action;
         if (actionPokemon.baseSpec.level != null || !(action.spawnInfo instanceof SpawnInfoPokemon)) {
            return;
         }

         SpawnInfoPokemon spawnInfo = (SpawnInfoPokemon)action.spawnInfo;
         EntityPixelmon pixelmon = (EntityPixelmon)actionPokemon.getOrCreateEntity();
         if (pixelmon.isBossPokemon()) {
            return;
         }

         int newLevel = this.getTweakedLevel(spawner, actionPokemon, actionPokemon.usingSpec.level, spawnInfo.minLevel, spawnInfo.maxLevel);
         pixelmon.getPokemonData().setLevel(newLevel);
         pixelmon.func_70606_j((float)pixelmon.getPokemonData().getHealth());
      } else if (action instanceof SpawnActionNPCTrainer) {
         SpawnActionNPCTrainer actionTrainer = (SpawnActionNPCTrainer)action;
         SpawnInfoNPCTrainer infoTrainer = (SpawnInfoNPCTrainer)actionTrainer.spawnInfo;
         NPCTrainer trainer = (NPCTrainer)actionTrainer.getOrCreateEntity();
         trainer.setLevel(this.getTweakedLevel(spawner, actionTrainer, trainer.level, infoTrainer.getMinLevel(), infoTrainer.getMaxLevel()));
      }

   }

   public int getTweakedLevel(AbstractSpawner spawner, SpawnAction action, int level, int min, int max) {
      if (min != max && action.spawnLocation.cause instanceof EntityPlayerMP) {
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
            int adjustedMin;
            int adjustedMax;
            if (td.highestLevel < 20) {
               adjustedMin = td.lowestLevel - 10;
               adjustedMax = td.highestLevel + 5;
            } else if (td.highestLevel < 40) {
               adjustedMin = td.lowestLevel - 20;
               adjustedMax = td.highestLevel + 15;
            } else {
               adjustedMin = 1;
               adjustedMax = PixelmonServerConfig.maxLevel;
            }

            if (action instanceof SpawnActionNPCTrainer && td.highestLevel >= 20) {
               adjustedMin = td.lowestLevel - 10;
               adjustedMax = td.highestLevel + 10;
            }

            return MathHelper.func_76125_a(RandomHelper.getRandomNumberBetween(adjustedMin, adjustedMax), min, max);
         }
      } else {
         return level;
      }
   }

   public boolean fits(AbstractSpawner spawner, SpawnInfo spawnInfo, SpawnLocation spawnLocation) {
      if (spawnLocation.cause instanceof EntityPlayerMP && spawner != PixelmonSpawning.fishingSpawner) {
         PlayerPartyStorage storage;
         if (spawner instanceof PlayerTrackingSpawner) {
            storage = ((PlayerTrackingSpawner)spawner).playerStorage;
         } else {
            storage = Pixelmon.storageManager.getParty((EntityPlayerMP)spawnLocation.cause);
         }

         if (storage == null) {
            return true;
         } else {
            TransientData td = storage.transientData;
            int adjustedMin;
            int adjustedMax;
            if (td.highestLevel < 25) {
               adjustedMin = td.lowestLevel - 10;
               adjustedMax = td.highestLevel + 5;
            } else {
               if (td.highestLevel >= 40) {
                  return true;
               }

               adjustedMin = td.lowestLevel - 20;
               adjustedMax = td.highestLevel + 15;
            }

            int min = MathHelper.func_76125_a(adjustedMin, 1, PixelmonServerConfig.maxLevel);
            int max = Math.min(adjustedMax, PixelmonServerConfig.maxLevel);
            if (spawnInfo instanceof SpawnInfoPokemon) {
               SpawnInfoPokemon spawn = (SpawnInfoPokemon)spawnInfo;
               PokemonSpec spec = spawn.getPokemonSpec();
               EnumSpecies species = spawn.getSpecies();
               if (spec != null && spec.name != null && spec.level == null && species != null) {
                  BaseStats bs = species.getBaseStats(species.getFormEnum(spec.form == null ? 0 : spec.form));
                  if (min <= bs.maxLevel && max >= bs.minLevel) {
                     if (spawn.minLevel <= max && spawn.maxLevel >= min) {
                        return true;
                     }

                     return false;
                  }

                  return false;
               }
            } else if (spawnInfo instanceof SpawnInfoNPCTrainer) {
               SpawnInfoNPCTrainer spawn = (SpawnInfoNPCTrainer)spawnInfo;
               if (spawn.getMinLevel() == spawn.getMaxLevel()) {
                  return true;
               }

               if (min > spawn.getMaxPartySize() || max < spawn.getMinLevel()) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }
}

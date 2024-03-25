package com.pixelmonmod.pixelmon.spawning;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.ISpawningTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs.trainers.SpawnActionNPCTrainer;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs.trainers.SpawnInfoNPCTrainer;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldType;

public class CaveLevelTweak implements ISpawningTweak {
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
      if (min != max && action.spawnLocation.cause instanceof EntityPlayerMP && action.spawnLocation.location != null && action.spawnLocation.location.pos != null) {
         EntityPlayerMP cause = (EntityPlayerMP)action.spawnLocation.cause;
         if (cause.func_71121_q().func_175624_G() == WorldType.field_77138_c) {
            return level;
         } else {
            int y = action.spawnLocation.location.pos.func_177956_o();
            double multiplier = (double)PixelmonConfig.caveMaxMultiplier * Math.exp(-0.05 * (double)y);
            return !(multiplier <= 1.0) && y < 31 ? (int)(multiplier * (double)level) : level;
         }
      } else {
         return level;
      }
   }
}

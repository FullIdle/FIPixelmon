package com.pixelmonmod.pixelmon.api.spawning.archetypes.spawners;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;

public class TriggerSpawner extends AbstractSpawner {
   public float triggerChance;

   public TriggerSpawner(String name) {
      this(name, 1.0F);
   }

   public TriggerSpawner(String name, float triggerChance) {
      super(name);
      this.triggerChance = triggerChance;
   }

   @Nullable
   public Entity trigger(SpawnLocation location) {
      if (RandomHelper.getRandomChance(this.triggerChance)) {
         SpawnInfo spawnInfo = this.getWeightedSpawnInfo(location);
         if (spawnInfo != null) {
            return spawnInfo.construct(this, location).doSpawn(this);
         }
      }

      return null;
   }

   public SpawnAction getAction(SpawnLocation location, float triggerChance) {
      if (RandomHelper.getRandomChance(triggerChance)) {
         SpawnInfo spawnInfo = this.getWeightedSpawnInfo(location);
         if (spawnInfo != null) {
            return spawnInfo.construct(this, location);
         }
      }

      return null;
   }
}

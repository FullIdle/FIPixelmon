package com.pixelmonmod.pixelmon.api.spawning.conditions;

import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;

public class RarityMultiplier {
   public float multiplier = 1.0F;
   public SpawnCondition condition = null;
   public SpawnCondition anticondition = null;

   public float apply(SpawnInfo spawnInfo, SpawnLocation spawnLocation, float rarity) {
      return this.condition != null && !this.condition.fits(spawnInfo, spawnLocation) || this.anticondition != null && !this.anticondition.fits(spawnInfo, spawnLocation) ? rarity : rarity * this.multiplier;
   }

   public void onImport() {
      if (this.condition != null) {
         this.condition.onImport();
      }

      if (this.anticondition != null) {
         this.anticondition.onImport();
      }

   }
}

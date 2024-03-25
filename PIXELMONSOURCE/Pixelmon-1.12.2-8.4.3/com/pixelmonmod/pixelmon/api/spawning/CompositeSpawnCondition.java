package com.pixelmonmod.pixelmon.api.spawning;

import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import java.util.ArrayList;

public class CompositeSpawnCondition {
   public ArrayList conditions = new ArrayList();
   public ArrayList anticonditions = new ArrayList();

   public CompositeSpawnCondition() {
   }

   public CompositeSpawnCondition(ArrayList conditions, ArrayList anticonditions) {
      this.conditions = conditions;
      this.anticonditions = anticonditions;
   }

   public boolean fits(SpawnInfo spawnInfo, SpawnLocation spawnLocation) {
      boolean fitsNormal = false;
      if (this.conditions != null && !this.conditions.isEmpty()) {
         for(int i = 0; i < this.conditions.size(); ++i) {
            if (((SpawnCondition)this.conditions.get(i)).fits(spawnInfo, spawnLocation)) {
               fitsNormal = true;
               break;
            }
         }
      } else {
         fitsNormal = true;
      }

      boolean fitsAnti = false;
      if (this.anticonditions != null && !this.anticonditions.isEmpty()) {
         for(int i = 0; i < this.anticonditions.size(); ++i) {
            if (((SpawnCondition)this.anticonditions.get(i)).fits(spawnInfo, spawnLocation)) {
               fitsAnti = true;
               break;
            }
         }
      }

      return fitsNormal && !fitsAnti;
   }

   public void onImport() {
      if (this.conditions != null) {
         this.conditions.forEach(SpawnCondition::onImport);
      }

      if (this.anticonditions != null) {
         this.anticonditions.forEach(SpawnCondition::onImport);
      }

   }
}

package com.pixelmonmod.pixelmon.api.events.spawning;

import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SpawnLocationEvent extends Event {
   private SpawnLocation spawnLocation;

   public SpawnLocationEvent(SpawnLocation spawnLocation) {
      this.spawnLocation = spawnLocation;
   }

   public void setSpawnLocation(SpawnLocation spawnLocation) {
      if (spawnLocation != null) {
         this.spawnLocation = spawnLocation;
      }

   }

   public SpawnLocation getSpawnLocation() {
      return this.spawnLocation;
   }
}

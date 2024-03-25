package com.pixelmonmod.pixelmon.api.events.spawning;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CreateSpawnerEvent extends Event {
   public final AbstractSpawner spawner;
   public final AbstractSpawner.SpawnerBuilder builder;

   public CreateSpawnerEvent(AbstractSpawner spawner, AbstractSpawner.SpawnerBuilder builder) {
      this.spawner = spawner;
      this.builder = builder;
   }
}

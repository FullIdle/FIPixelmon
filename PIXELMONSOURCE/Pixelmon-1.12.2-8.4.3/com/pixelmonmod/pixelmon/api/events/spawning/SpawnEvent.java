package com.pixelmonmod.pixelmon.api.events.spawning;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SpawnEvent extends Event {
   public final AbstractSpawner spawner;
   public final SpawnAction action;

   public SpawnEvent(AbstractSpawner spawner, SpawnAction action) {
      this.spawner = spawner;
      this.action = action;
   }
}

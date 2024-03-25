package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;

public class SpawnInfoNPC extends SpawnInfo {
   public static final String TYPE_ID_NPC = "npc";
   public String name;

   public SpawnInfoNPC() {
      super("npc");
   }

   public SpawnAction construct(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      return new SpawnActionNPC(this, spawnLocation);
   }

   public String toString() {
      return this.name;
   }
}

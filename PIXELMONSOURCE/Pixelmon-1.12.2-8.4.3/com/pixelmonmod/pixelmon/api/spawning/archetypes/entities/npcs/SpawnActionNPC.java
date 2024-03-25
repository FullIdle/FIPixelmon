package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.npcs;

import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;

public class SpawnActionNPC extends SpawnAction {
   public String name;

   public SpawnActionNPC(SpawnInfoNPC spawnInfo, SpawnLocation spawnLocation) {
      super(spawnInfo, spawnLocation);
      this.name = spawnInfo.name;
   }

   protected EntityNPC createEntity() {
      EntityNPC npc = (EntityNPC)PixelmonEntityList.createEntityByName(this.name, this.spawnLocation.location.world, this.spawnLocation.biome.field_76791_y);
      return npc;
   }
}

package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.wormholes;

import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;

public class SpawnActionWormhole extends SpawnAction {
   public SpawnActionWormhole(SpawnInfoWormhole spawnInfo, SpawnLocation spawnLocation) {
      super(spawnInfo, spawnLocation);
   }

   protected EntityWormhole createEntity() {
      return new EntityWormhole(this.spawnLocation.location.world, (double)this.spawnLocation.location.pos.func_177958_n(), (double)this.spawnLocation.location.pos.func_177956_o(), (double)this.spawnLocation.location.pos.func_177952_p(), ((SpawnInfoWormhole)this.spawnInfo).minMaxAge + this.spawnLocation.location.world.field_73012_v.nextInt(((SpawnInfoWormhole)this.spawnInfo).maxMaxAge - ((SpawnInfoWormhole)this.spawnInfo).minMaxAge));
   }
}

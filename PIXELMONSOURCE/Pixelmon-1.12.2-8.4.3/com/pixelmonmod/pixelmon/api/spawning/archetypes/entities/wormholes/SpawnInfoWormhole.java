package com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.wormholes;

import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnAction;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraft.util.text.translation.I18n;

public class SpawnInfoWormhole extends SpawnInfo {
   public static final String TYPE_ID_WORMHOLE = "wormhole";
   public int minMaxAge = 6000;
   public int maxMaxAge = 12000;

   public SpawnInfoWormhole() {
      super("wormhole");
   }

   public SpawnAction construct(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      return new SpawnActionWormhole(this, spawnLocation);
   }

   public boolean fits(AbstractSpawner spawner, SpawnLocation spawnLocation) {
      return PixelmonConfig.ultraSpace && super.fits(spawner, spawnLocation);
   }

   public String toString() {
      return I18n.func_74838_a("pixelmon.ultraspace.wormhole.name");
   }
}

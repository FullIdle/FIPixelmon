package com.pixelmonmod.pixelmon.blocks.spawning;

import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Rarity;
import java.util.List;

public class BlockSpawnData {
   public final String name;
   public int minLvl;
   public int maxLvl;
   public Rarity rarity;

   public BlockSpawnData(String name, int minLvl, int maxLvl, Rarity rarity) {
      this.name = name;
      this.maxLvl = maxLvl;
      this.minLvl = minLvl;
      this.rarity = rarity;
   }

   public int getRarity(List times) {
      int intRarity;
      if (times.contains(WorldTime.DAY)) {
         intRarity = this.rarity.day;
      } else if (!times.contains(WorldTime.DAWN) && !times.contains(WorldTime.DUSK)) {
         intRarity = this.rarity.night;
      } else {
         intRarity = this.rarity.dawndusk;
      }

      return intRarity;
   }
}

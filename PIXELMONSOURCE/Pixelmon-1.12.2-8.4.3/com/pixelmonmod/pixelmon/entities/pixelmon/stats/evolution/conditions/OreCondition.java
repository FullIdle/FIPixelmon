package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MeltanStats;

public class OreCondition extends EvoCondition {
   public int ores;

   public OreCondition() {
      super("ores");
   }

   public OreCondition(int ores) {
      this();
      this.ores = ores;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      MeltanStats extraStats = (MeltanStats)pixelmon.getPokemonData().getExtraStats(MeltanStats.class);
      return extraStats != null && extraStats.oresSmelted > this.ores;
   }
}

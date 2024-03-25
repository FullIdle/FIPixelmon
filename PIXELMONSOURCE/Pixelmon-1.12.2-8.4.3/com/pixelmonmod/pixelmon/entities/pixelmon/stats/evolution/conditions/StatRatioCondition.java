package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class StatRatioCondition extends EvoCondition {
   public StatsType stat1;
   public StatsType stat2;
   public float ratio;

   public StatRatioCondition() {
      super("statRatio");
      this.ratio = 1.0F;
   }

   public StatRatioCondition(StatsType stat1, StatsType stat2, float ratio) {
      this();
      this.stat1 = stat1;
      this.stat2 = stat2;
      this.ratio = ratio;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return 1.0F * (float)pixelmon.getPokemonData().getStat(this.stat1) / (float)pixelmon.getPokemonData().getStat(this.stat2) > this.ratio;
   }
}

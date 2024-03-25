package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.RecoilStats;

public class RecoilDamageCondition extends EvoCondition {
   int recoil;

   public RecoilDamageCondition() {
      super("recoil");
   }

   public boolean passes(EntityPixelmon pixelmon) {
      RecoilStats stats = (RecoilStats)pixelmon.getPokemonData().getExtraStats(RecoilStats.class);
      if (stats != null) {
         return stats.recoil() > this.recoil;
      } else {
         return false;
      }
   }
}

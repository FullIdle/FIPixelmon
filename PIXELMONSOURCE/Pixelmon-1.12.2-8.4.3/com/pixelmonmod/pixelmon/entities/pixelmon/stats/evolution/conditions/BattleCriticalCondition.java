package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class BattleCriticalCondition extends EvoCondition {
   public int critical;

   public BattleCriticalCondition() {
      super("critical");
   }

   public BattleCriticalCondition(int critical) {
      this();
      this.critical = critical;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.getPokemonData().lastBattleCrits >= this.critical;
   }
}

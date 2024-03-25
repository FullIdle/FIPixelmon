package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class InvertCondition extends EvoCondition {
   EvoCondition condition = null;

   public InvertCondition() {
      super("invert");
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return !this.condition.passes(pixelmon);
   }
}

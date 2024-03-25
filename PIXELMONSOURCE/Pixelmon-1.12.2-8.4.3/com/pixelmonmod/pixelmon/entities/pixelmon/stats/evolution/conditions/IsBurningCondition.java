package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class IsBurningCondition extends EvoCondition {
   public IsBurningCondition() {
      super("isBurning");
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.func_70027_ad();
   }
}

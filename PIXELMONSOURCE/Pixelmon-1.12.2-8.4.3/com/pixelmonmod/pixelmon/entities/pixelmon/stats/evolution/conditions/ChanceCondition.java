package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class ChanceCondition extends EvoCondition {
   public float chance;

   public ChanceCondition() {
      super("chance");
      this.chance = 0.5F;
   }

   public ChanceCondition(float chance) {
      this();
      this.chance = chance;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return RandomHelper.getRandomChance(this.chance);
   }
}

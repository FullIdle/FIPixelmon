package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class LevelCondition extends EvoCondition {
   public int level;

   public LevelCondition() {
      super("level");
   }

   public LevelCondition(int level) {
      this();
      this.level = level;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.getLvl().getLevel() >= this.level;
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class MoonPhaseCondition extends EvoCondition {
   public int phase;

   public MoonPhaseCondition() {
      super("moonPhase");
      this.phase = 0;
   }

   public MoonPhaseCondition(int phase) {
      this();
      this.phase = phase;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      return pixelmon.field_70170_p.field_73011_w.func_76559_b(pixelmon.field_70170_p.func_72820_D()) == this.phase;
   }
}

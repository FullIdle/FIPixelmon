package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class LightScreen extends Screen {
   public LightScreen() {
      this(5);
   }

   public LightScreen(int turns) {
      super(StatusType.LightScreen, StatsType.SpecialDefence, turns, "pixelmon.effect.uplightscreen", "pixelmon.effect.alreadylightscreen", "pixelmon.status.lightscreenoff");
   }

   protected Screen getNewInstance(int effectTurns) {
      return new LightScreen(effectTurns);
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class GuardSwap extends Swap {
   public GuardSwap() {
      super("pixelmon.effect.guardswap", StatsType.Defence, StatsType.SpecialDefence);
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class PowerSwap extends Swap {
   public PowerSwap() {
      super("pixelmon.effect.powerswap", StatsType.Attack, StatsType.SpecialAttack);
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Defeatist extends AbilityBase {
   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.getHealthPercent() <= 50.0F) {
         int var10001 = StatsType.Attack.getStatIndex();
         stats[var10001] /= 2;
         var10001 = StatsType.SpecialAttack.getStatIndex();
         stats[var10001] /= 2;
      }

      return stats;
   }
}

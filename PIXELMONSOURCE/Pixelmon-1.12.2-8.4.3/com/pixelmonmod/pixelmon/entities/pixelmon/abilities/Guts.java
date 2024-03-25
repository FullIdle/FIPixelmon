package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Guts extends AbilityBase {
   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.hasPrimaryStatus()) {
         int var10001 = StatsType.Attack.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class MarvelScale extends AbilityBase {
   public int[] modifyStatsCancellable(PixelmonWrapper user, int[] stats) {
      if (user.hasPrimaryStatus()) {
         int var10001 = StatsType.Defence.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }
}

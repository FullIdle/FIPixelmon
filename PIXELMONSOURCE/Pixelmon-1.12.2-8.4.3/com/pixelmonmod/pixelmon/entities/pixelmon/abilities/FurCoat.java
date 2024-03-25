package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class FurCoat extends AbilityBase {
   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      int var10001 = StatsType.Defence.getStatIndex();
      stats[var10001] *= 2;
      return stats;
   }
}

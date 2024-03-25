package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class GrassPledge extends PledgeBase {
   public GrassPledge() {
      super(StatusType.GrassPledge);
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      int var10001 = StatsType.Speed.getStatIndex();
      stats[var10001] /= 2;
      return stats;
   }
}

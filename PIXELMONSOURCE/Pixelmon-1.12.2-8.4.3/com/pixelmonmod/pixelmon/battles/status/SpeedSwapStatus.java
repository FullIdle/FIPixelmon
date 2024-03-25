package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SpeedSwapStatus extends StatusBase {
   private int newSpeed = 0;

   public SpeedSwapStatus() {
      super(StatusType.SpeedSwap);
   }

   public SpeedSwapStatus(int newSpeed) {
      super(StatusType.SpeedSwap);
      this.newSpeed = newSpeed;
   }

   public void applyBeforeEffect(PixelmonWrapper victim, PixelmonWrapper opponent) {
      if (!victim.bc.simulateMode) {
         victim.getBattleStats().speedStat = this.newSpeed;
      }

   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      stats[StatsType.Speed.getStatIndex()] = this.newSpeed;
      return stats;
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class TangledFeet extends AbilityBase {
   public int[] modifyStatsCancellable(PixelmonWrapper user, int[] stats) {
      if (user.hasStatus(StatusType.Confusion)) {
         int var10001 = StatsType.Evasion.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.2);
      }

      return stats;
   }
}

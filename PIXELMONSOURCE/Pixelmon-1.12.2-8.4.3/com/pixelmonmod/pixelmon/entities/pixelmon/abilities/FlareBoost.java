package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class FlareBoost extends AbilityBase {
   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.hasStatus(StatusType.Burn)) {
         int var10001 = StatsType.SpecialAttack.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }
}

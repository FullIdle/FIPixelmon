package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Hail;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SnowCloak extends AbilityBase {
   public int[] modifyStatsCancellable(PixelmonWrapper user, int[] stats) {
      if (user.bc.globalStatusController.getWeather() instanceof Hail) {
         int var10001 = StatsType.Evasion.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.25);
      }

      return stats;
   }
}

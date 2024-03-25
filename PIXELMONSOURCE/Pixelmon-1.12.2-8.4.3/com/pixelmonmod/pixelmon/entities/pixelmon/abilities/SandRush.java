package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sandstorm;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SandRush extends AbilityBase {
   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.bc.globalStatusController.getWeather() instanceof Sandstorm) {
         int var10001 = StatsType.Speed.getStatIndex();
         stats[var10001] *= 2;
      }

      return stats;
   }
}

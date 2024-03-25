package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class SurgeSurfer extends AbilityBase {
   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.bc.globalStatusController.hasStatus(StatusType.ElectricTerrain)) {
         int var10001 = StatsType.Speed.getStatIndex();
         stats[var10001] *= 2;
      }

      return stats;
   }
}

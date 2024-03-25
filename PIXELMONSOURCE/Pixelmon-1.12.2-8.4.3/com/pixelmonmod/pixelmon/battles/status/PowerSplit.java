package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class PowerSplit extends Split {
   public PowerSplit() {
      super(StatusType.PowerSplit, StatsType.Attack, StatsType.SpecialAttack, "pixelmon.status.powersplit");
   }

   protected Split getNewInstance(int splitStat1, int splitStat2) {
      PowerSplit split = new PowerSplit();
      split.statValue1 = splitStat1;
      split.statValue2 = splitStat2;
      return split;
   }
}

package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class GuardSplit extends Split {
   public GuardSplit() {
      super(StatusType.GuardSplit, StatsType.Defence, StatsType.SpecialDefence, "pixelmon.status.guardsplit");
   }

   protected Split getNewInstance(int splitStat1, int splitStat2) {
      GuardSplit split = new GuardSplit();
      split.statValue1 = splitStat1;
      split.statValue2 = splitStat2;
      return split;
   }
}

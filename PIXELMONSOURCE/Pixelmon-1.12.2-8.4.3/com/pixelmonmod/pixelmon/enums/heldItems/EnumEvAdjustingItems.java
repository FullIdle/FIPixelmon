package com.pixelmonmod.pixelmon.enums.heldItems;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public enum EnumEvAdjustingItems {
   MachoBrace(StatsType.None),
   PowerWeight(StatsType.HP),
   PowerBracer(StatsType.Attack),
   PowerBelt(StatsType.Defence),
   PowerLens(StatsType.SpecialAttack),
   PowerBand(StatsType.SpecialDefence),
   PowerAnklet(StatsType.Speed);

   public StatsType statAffected;

   private EnumEvAdjustingItems(StatsType statAffected) {
      this.statAffected = statAffected;
   }
}

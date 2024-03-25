package com.pixelmonmod.pixelmon.enums.heldItems;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public enum EnumChoiceItems {
   ChoiceBand(StatsType.Attack),
   ChoiceScarf(StatsType.Speed),
   ChoiceSpecs(StatsType.SpecialAttack);

   public StatsType effectType;

   private EnumChoiceItems(StatsType type) {
      this.effectType = type;
   }
}

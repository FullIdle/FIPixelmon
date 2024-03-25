package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemEviolite extends StatEnhancingItems {
   public ItemEviolite() {
      super(EnumHeldItems.eviolite, "eviolite", new StatsType[]{StatsType.Defence, StatsType.SpecialDefence}, 1.5F);
   }

   protected boolean canAffect(PixelmonWrapper pokemon) {
      return !pokemon.getBaseStats().getEvolutions().isEmpty();
   }
}

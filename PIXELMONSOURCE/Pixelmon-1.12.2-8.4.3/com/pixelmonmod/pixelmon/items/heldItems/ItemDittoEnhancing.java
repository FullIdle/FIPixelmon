package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemDittoEnhancing extends StatEnhancingItems {
   public ItemDittoEnhancing(EnumHeldItems type, String itemName, StatsType[] raisedStats, float statMultiplier) {
      super(type, itemName, raisedStats, statMultiplier, EnumSpecies.Ditto);
   }

   protected boolean canAffect(PixelmonWrapper pokemon) {
      return super.canAffect(pokemon) && !pokemon.hasStatus(StatusType.Transformed);
   }
}

package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemDeepSeaToothScale extends ItemHeld {
   public ItemDeepSeaToothScale(EnumHeldItems heldItemType, String itemName) {
      super(heldItemType, itemName);
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.getSpecies() != EnumSpecies.Clamperl) {
         return stats;
      } else {
         int var10001;
         if (this.getHeldItemType() == EnumHeldItems.deepSeaScale) {
            var10001 = StatsType.SpecialDefence.getStatIndex();
            stats[var10001] *= 2;
         } else {
            var10001 = StatsType.SpecialAttack.getStatIndex();
            stats[var10001] *= 2;
         }

         return stats;
      }
   }
}

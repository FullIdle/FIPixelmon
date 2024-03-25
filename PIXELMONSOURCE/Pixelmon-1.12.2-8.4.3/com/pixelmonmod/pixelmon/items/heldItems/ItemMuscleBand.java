package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemMuscleBand extends ItemHeld {
   StatsType stat;

   public ItemMuscleBand(EnumHeldItems HeldItemType, String itemName, StatsType stat) {
      super(HeldItemType, itemName);
      this.stat = stat;
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      int var10001 = this.stat.getStatIndex();
      stats[var10001] = (int)((double)stats[var10001] * 1.1);
      return stats;
   }
}

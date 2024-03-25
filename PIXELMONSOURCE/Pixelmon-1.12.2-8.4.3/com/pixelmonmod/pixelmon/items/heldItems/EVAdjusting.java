package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumEvAdjustingItems;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class EVAdjusting extends ItemHeld {
   public EnumEvAdjustingItems type;

   public EVAdjusting(EnumEvAdjustingItems type, String itemName) {
      super(EnumHeldItems.evAdjusting, itemName);
      this.type = type;
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      int var10001 = StatsType.Speed.getStatIndex();
      stats[var10001] = (int)((double)stats[var10001] * 0.5);
      return stats;
   }
}

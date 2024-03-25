package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemWideLens extends ItemHeld {
   public ItemWideLens() {
      super(EnumHeldItems.wideLens, "wide_lens");
   }

   public int[] modifyPowerAndAccuracyUser(int[] modifiedMoveStats, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      modifiedMoveStats[1] = (int)((double)modifiedMoveStats[1] * 1.1);
      return modifiedMoveStats;
   }
}

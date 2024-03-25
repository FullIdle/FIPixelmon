package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemZoomLens extends ItemHeld {
   public ItemZoomLens() {
      super(EnumHeldItems.zoomLens, "zoom_lens");
   }

   public int[] modifyPowerAndAccuracyUser(int[] modifiedMoveStats, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (target.bc.battleLog.getActionForPokemon(target.bc.battleTurn, target) != null || target.attack == null) {
         modifiedMoveStats[1] = (int)((double)modifiedMoveStats[1] * 1.2);
      }

      return modifiedMoveStats;
   }
}

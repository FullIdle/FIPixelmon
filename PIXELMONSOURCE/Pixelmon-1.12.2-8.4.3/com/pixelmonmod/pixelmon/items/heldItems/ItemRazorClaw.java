package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemRazorClaw extends ItemHeld {
   public ItemRazorClaw(EnumHeldItems heldItemType, String itemName) {
      super(heldItemType, itemName);
   }

   public int adjustCritStage(PixelmonWrapper user) {
      return 1;
   }
}

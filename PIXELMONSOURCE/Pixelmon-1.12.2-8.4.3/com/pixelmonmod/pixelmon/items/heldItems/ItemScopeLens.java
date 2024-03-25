package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemScopeLens extends ItemHeld {
   public ItemScopeLens() {
      super(EnumHeldItems.scopeLens, "scope_lens");
   }

   public int adjustCritStage(PixelmonWrapper user) {
      return 1;
   }
}

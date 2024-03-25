package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class NoItem extends ItemHeld {
   public static final NoItem noItem = new NoItem();

   private NoItem() {
      super(EnumHeldItems.other, "");
   }
}

package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.items.EnumZCrystals;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemZCrystal extends ItemHeld {
   public EnumZCrystals type;

   public ItemZCrystal(EnumZCrystals type) {
      super(EnumHeldItems.zCrystal, type.getFileName());
      this.type = type;
   }
}

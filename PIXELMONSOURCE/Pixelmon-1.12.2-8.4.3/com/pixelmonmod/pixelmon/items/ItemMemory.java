package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemMemory extends ItemHeld {
   public EnumType type;

   public ItemMemory(String itemName, EnumType type) {
      super(EnumHeldItems.memory, itemName);
      this.type = type;
   }
}

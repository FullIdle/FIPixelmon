package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemFloatStone extends ItemHeld {
   public ItemFloatStone() {
      super(EnumHeldItems.floatStone, "float_stone");
   }

   public float modifyWeight(float initWeight) {
      return initWeight / 2.0F;
   }
}

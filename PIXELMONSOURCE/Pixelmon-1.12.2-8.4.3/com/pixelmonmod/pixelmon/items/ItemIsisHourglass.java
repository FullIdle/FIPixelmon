package com.pixelmonmod.pixelmon.items;

import net.minecraft.creativetab.CreativeTabs;

public class ItemIsisHourglass extends PixelmonItem {
   public EnumCheatItemType type;

   public ItemIsisHourglass(EnumCheatItemType type) {
      super("hourglass_" + type.toString().toLowerCase());
      this.type = type;
      this.func_77637_a(CreativeTabs.field_78040_i);
   }
}

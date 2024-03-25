package com.pixelmonmod.pixelmon.items;

import net.minecraft.creativetab.CreativeTabs;

public class ItemLureCasing extends PixelmonItem {
   public final ItemLure.LureStrength strength;

   public ItemLureCasing(ItemLure.LureStrength strength) {
      super("lure_casing_" + strength.name().toLowerCase());
      this.strength = strength;
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.func_77625_d(64);
   }
}

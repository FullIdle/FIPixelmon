package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.EnumCurryKey;

public class ItemCurryKey extends PixelmonItem {
   private final EnumCurryKey key;

   public ItemCurryKey(EnumCurryKey key) {
      super("curry_" + key.name().toLowerCase());
      this.key = key;
      this.func_77637_a(PixelmonCreativeTabs.restoration);
   }

   public EnumCurryKey getKey() {
      return this.key;
   }
}

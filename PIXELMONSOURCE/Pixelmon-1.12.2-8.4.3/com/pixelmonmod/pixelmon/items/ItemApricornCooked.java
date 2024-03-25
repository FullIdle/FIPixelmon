package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;

public class ItemApricornCooked extends PixelmonItem {
   public EnumApricorns apricorn;

   public ItemApricornCooked(EnumApricorns apricorn) {
      super("cooked_" + apricorn.toString().toLowerCase() + "_apricorn");
      this.apricorn = apricorn;
      this.func_77625_d(64);
      this.func_77656_e(0);
      this.func_77637_a(PixelmonCreativeTabs.natural);
      this.canRepair = false;
   }
}

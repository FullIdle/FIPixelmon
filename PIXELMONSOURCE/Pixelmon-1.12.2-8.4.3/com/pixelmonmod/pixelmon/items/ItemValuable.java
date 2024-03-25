package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.enums.items.EnumValuables;
import net.minecraft.creativetab.CreativeTabs;

public class ItemValuable extends PixelmonItem {
   public EnumValuables valuable;

   public ItemValuable(EnumValuables valuable) {
      super(valuable.getFileName());
      this.valuable = valuable;
      this.func_77625_d(64);
      this.func_77656_e(0);
      this.func_77637_a(CreativeTabs.field_78026_f);
      this.canRepair = false;
   }
}

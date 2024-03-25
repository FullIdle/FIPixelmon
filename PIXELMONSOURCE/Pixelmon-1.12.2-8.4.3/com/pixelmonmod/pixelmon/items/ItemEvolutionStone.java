package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.enums.EnumEvolutionStone;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEvolutionStone extends PixelmonItem {
   private final EnumEvolutionStone stoneType;

   public ItemEvolutionStone(EnumEvolutionStone stoneType, String itemName) {
      super(itemName);
      this.stoneType = stoneType;
      this.func_77637_a(CreativeTabs.field_78026_f);
      this.canRepair = false;
   }

   public EnumEvolutionStone getType() {
      return this.stoneType;
   }
}

package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMint extends PixelmonItem {
   public ItemMint() {
      super("mint");
      this.func_77627_a(true);
      this.func_77656_e(0);
      this.func_77625_d(16);
      this.func_77637_a(CreativeTabs.field_78026_f);
      this.canRepair = false;
   }

   public String func_77667_c(ItemStack stack) {
      int i = stack.func_77960_j();
      return super.func_77658_a() + "." + EnumNature.values()[i].func_176610_l().toLowerCase();
   }

   public void func_150895_a(CreativeTabs tab, NonNullList items) {
      if (this.func_194125_a(tab)) {
         for(int i = 0; i < EnumNature.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
         }
      }

   }

   public static EnumNature getNature(ItemStack stack) {
      int meta = stack.func_77960_j();
      return meta >= 0 && meta < EnumNature.values().length ? EnumNature.values()[meta] : null;
   }

   public static ItemStack createStack(EnumNature nature, int size) {
      return new ItemStack(PixelmonItems.mint, size, nature.ordinal());
   }
}

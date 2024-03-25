package com.pixelmonmod.pixelmon.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GenericItem extends Item {
   public GenericItem(String name) {
      this.func_77655_b(name);
      this.setRegistryName(name);
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }
}

package com.pixelmonmod.pixelmon.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;

public class ItemHiddenDoor extends ItemDoor {
   public ItemHiddenDoor(Block doorBlock, String itemName) {
      super(doorBlock);
      this.func_77637_a(CreativeTabs.field_78028_d);
      this.func_77625_d(16);
      this.func_77655_b(itemName);
      this.setRegistryName(itemName);
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }
}

package com.pixelmonmod.pixelmon.blocks.tileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public interface IBasicInventory extends IInventory {
   default ItemStack func_70298_a(int index, int count) {
      if (this.func_70301_a(index) != null) {
         ItemStack itemstack;
         if (this.func_70301_a(index).func_190916_E() <= count) {
            itemstack = this.func_70301_a(index);
            this.func_70304_b(index);
            return itemstack;
         } else {
            itemstack = this.func_70301_a(index).func_77979_a(count);
            if (this.func_70301_a(index).func_190916_E() == 0) {
               this.func_70304_b(index);
            }

            return itemstack;
         }
      } else {
         return null;
      }
   }

   default int func_70297_j_() {
      return 64;
   }

   default void func_174889_b(EntityPlayer player) {
   }

   default void func_174886_c(EntityPlayer player) {
   }

   default boolean func_94041_b(int index, ItemStack stack) {
      return true;
   }

   default int func_174887_a_(int id) {
      return 0;
   }

   default void func_174885_b(int id, int value) {
   }

   default int func_174890_g() {
      return 0;
   }

   default void func_174888_l() {
      for(int i = 0; i < this.func_70302_i_(); ++i) {
         this.func_70304_b(i);
      }

   }

   default boolean func_145818_k_() {
      return false;
   }

   default ITextComponent func_145748_c_() {
      return new TextComponentTranslation(this.func_70005_c_(), new Object[0]);
   }
}

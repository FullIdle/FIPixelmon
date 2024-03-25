package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class PorygonPiece extends Item {
   public PorygonPiece(String name) {
      this.func_77637_a(CreativeTabs.field_78026_f);
      this.func_77655_b(name);
      this.setRegistryName(name);
      this.func_77627_a(true);
      this.func_77656_e(0);
   }

   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
      if (stack.func_77952_i() == 0) {
         stack.func_190920_e(0);
         if (!worldIn.field_72995_K) {
            Pixelmon.pokemonFactory.create(EnumSpecies.Porygon).getOrSpawnPixelmon(worldIn, entityIn.field_70165_t, entityIn.field_70163_u + 2.0, entityIn.field_70161_v);
         }
      }

   }

   public int getItemStackLimit(ItemStack stack) {
      return stack.func_77952_i() == 0 ? 1 : 8;
   }

   public void func_150895_a(CreativeTabs tab, NonNullList items) {
      if (this.func_194125_a(tab)) {
         for(int i = 0; i < 5; ++i) {
            items.add(new ItemStack(this, 1, i));
         }
      }

   }

   public String func_77667_c(ItemStack stack) {
      if (stack.func_77952_i() > 0) {
         if (stack.func_77952_i() == 1) {
            return super.func_77667_c(stack) + "_head";
         }

         if (stack.func_77952_i() == 2) {
            return super.func_77667_c(stack) + "_body";
         }

         if (stack.func_77952_i() == 3) {
            return super.func_77667_c(stack) + "_leg";
         }

         if (stack.func_77952_i() == 4) {
            return super.func_77667_c(stack) + "_tail";
         }
      }

      return super.func_77667_c(stack);
   }
}

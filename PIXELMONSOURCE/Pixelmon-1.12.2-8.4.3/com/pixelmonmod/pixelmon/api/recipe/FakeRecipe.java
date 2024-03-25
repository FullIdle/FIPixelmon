package com.pixelmonmod.pixelmon.api.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class FakeRecipe extends IForgeRegistryEntry.Impl implements IRecipe {
   public boolean func_194133_a(int width, int height) {
      return false;
   }

   public ItemStack func_77572_b(InventoryCrafting inv) {
      return ItemStack.field_190927_a;
   }

   public ItemStack func_77571_b() {
      return ItemStack.field_190927_a;
   }

   public boolean func_77569_a(InventoryCrafting inv, World worldIn) {
      return false;
   }
}

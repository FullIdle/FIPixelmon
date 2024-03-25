package com.pixelmonmod.pixelmon.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FurnaceRecipeFactory implements IRecipeFactory {
   public IRecipe parse(JsonContext context, JsonObject json) {
      ItemStack result = CraftingHelper.getItemStack(JsonUtils.func_152754_s(json, "result"), context);
      ItemStack input = CraftingHelper.getItemStack(JsonUtils.func_152754_s(json, "input"), context);
      float xp = JsonUtils.func_151217_k(json, "xp");
      GameRegistry.addSmelting(input, result, xp);
      return new FakeRecipe();
   }
}

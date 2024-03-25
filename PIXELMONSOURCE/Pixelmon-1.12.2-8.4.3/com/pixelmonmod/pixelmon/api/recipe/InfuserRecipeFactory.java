package com.pixelmonmod.pixelmon.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class InfuserRecipeFactory implements IRecipeFactory {
   public IRecipe parse(JsonContext context, JsonObject json) {
      ItemStack result = CraftingHelper.getItemStack(JsonUtils.func_152754_s(json, "result"), context);
      ItemStack input1 = CraftingHelper.getItemStack(JsonUtils.func_152754_s(json, "input1"), context);
      ItemStack input2 = CraftingHelper.getItemStack(JsonUtils.func_152754_s(json, "input2"), context);
      int ticks = JsonUtils.func_151208_a(json, "ticks", 200);
      InfuserRecipes.instance().addRecipeForIngredients(input1, input2, ticks, result);
      return new FakeRecipe();
   }
}

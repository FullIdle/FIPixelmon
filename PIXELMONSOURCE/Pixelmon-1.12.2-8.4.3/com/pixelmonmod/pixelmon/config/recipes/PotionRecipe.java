package com.pixelmonmod.pixelmon.config.recipes;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import javax.annotation.Nonnull;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class PotionRecipe implements IBrewingRecipe {
   public boolean isInput(@Nonnull ItemStack input) {
      return isWaterBottle(input) || input.func_77973_b() == PixelmonItems.potion || input.func_77973_b() == PixelmonItems.superPotion;
   }

   public boolean isIngredient(@Nonnull ItemStack ingredient) {
      Item item = ingredient.func_77973_b();
      if (!(item instanceof ItemBerry)) {
         return false;
      } else {
         EnumBerry type = ((ItemBerry)item).getBerry();
         return type == EnumBerry.Oran || type == EnumBerry.Leppa || type == EnumBerry.Sitrus || type == EnumBerry.Figy || type == EnumBerry.Wiki || type == EnumBerry.Mago || type == EnumBerry.Aguav || type == EnumBerry.Iapapa;
      }
   }

   @Nonnull
   public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
      if (!(ingredient.func_77973_b() instanceof ItemBerry)) {
         return ItemStack.field_190927_a;
      } else {
         EnumBerry berry = ((ItemBerry)ingredient.func_77973_b()).getBerry();
         if (!isWaterBottle(input) || berry != EnumBerry.Oran && berry != EnumBerry.Leppa) {
            if (input.func_77973_b() == PixelmonItems.potion && berry == EnumBerry.Sitrus) {
               return new ItemStack(PixelmonItems.superPotion);
            } else {
               return input.func_77973_b() != PixelmonItems.superPotion || berry != EnumBerry.Figy && berry != EnumBerry.Wiki && berry != EnumBerry.Mago && berry != EnumBerry.Aguav && berry != EnumBerry.Iapapa ? ItemStack.field_190927_a : new ItemStack(PixelmonItems.hyperPotion);
            }
         } else {
            return new ItemStack(PixelmonItems.potion);
         }
      }
   }

   public static boolean isWaterBottle(ItemStack stack) {
      return stack.func_77973_b() == Items.field_151068_bn && stack.func_77942_o() && stack.func_77978_p().func_74779_i("Potion").equalsIgnoreCase("minecraft:water");
   }
}

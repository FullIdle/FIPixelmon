package com.pixelmonmod.pixelmon.config.recipes;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import javax.annotation.Nonnull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class StatusHealRecipe implements IBrewingRecipe {
   public boolean isInput(@Nonnull ItemStack input) {
      return PotionRecipe.isWaterBottle(input);
   }

   public boolean isIngredient(@Nonnull ItemStack ingredient) {
      Item item = ingredient.func_77973_b();
      if (!(item instanceof ItemBerry)) {
         return false;
      } else {
         EnumBerry type = ((ItemBerry)item).getBerry();
         return type == EnumBerry.Cheri || type == EnumBerry.Chesto || type == EnumBerry.Pecha || type == EnumBerry.Rawst || type == EnumBerry.Aspear || type == EnumBerry.Lum;
      }
   }

   @Nonnull
   public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
      if (!(ingredient.func_77973_b() instanceof ItemBerry)) {
         return ItemStack.field_190927_a;
      } else if (!PotionRecipe.isWaterBottle(input)) {
         return ItemStack.field_190927_a;
      } else {
         ItemBerry berry = (ItemBerry)ingredient.func_77973_b();
         switch (berry.getBerry()) {
            case Cheri:
               return new ItemStack(PixelmonItems.parlyzHeal);
            case Chesto:
               return new ItemStack(PixelmonItems.awakening);
            case Pecha:
               return new ItemStack(PixelmonItems.antidote);
            case Aspear:
               return new ItemStack(PixelmonItems.iceHeal);
            case Lum:
               return new ItemStack(PixelmonItems.fullHeal);
            case Rawst:
               return new ItemStack(PixelmonItems.burnHeal);
            default:
               return ItemStack.field_190927_a;
         }
      }
   }
}

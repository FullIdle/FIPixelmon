package com.pixelmonmod.pixelmon.config.recipes;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.enums.EnumBerry;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import javax.annotation.Nonnull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class StatsRecipe implements IBrewingRecipe {
   public boolean isInput(@Nonnull ItemStack input) {
      return PotionRecipe.isWaterBottle(input);
   }

   public boolean isIngredient(@Nonnull ItemStack ingredient) {
      Item item = ingredient.func_77973_b();
      if (!(item instanceof ItemBerry)) {
         return false;
      } else {
         EnumBerry type = ((ItemBerry)item).getBerry();
         return type == EnumBerry.Liechi || type == EnumBerry.Ganlon || type == EnumBerry.Salac || type == EnumBerry.Petaya || type == EnumBerry.Apicot || type == EnumBerry.Lansat || type == EnumBerry.Micle;
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
            case Liechi:
               return new ItemStack(PixelmonItems.xAttack);
            case Ganlon:
               return new ItemStack(PixelmonItems.xDefence);
            case Salac:
               return new ItemStack(PixelmonItems.xSpeed);
            case Petaya:
               return new ItemStack(PixelmonItems.xSpecialAttack);
            case Apicot:
               return new ItemStack(PixelmonItems.xSpecialDefence);
            case Lansat:
               return new ItemStack(PixelmonItems.direHit);
            case Micle:
               return new ItemStack(PixelmonItems.xAccuracy);
            default:
               return ItemStack.field_190927_a;
         }
      }
   }
}

package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public enum EnumCurryKey {
   NONE("plain"),
   BREAD("toast"),
   PASTA("pasta"),
   FRIED_FOOD("fried"),
   INSTANT_NOODLES("instant_noodle"),
   PRECOOKED_BURGER("burger"),
   SAUSAGES("sausage"),
   BEAN_TIN("beans"),
   MIXED_MUSHROOMS("mushroom"),
   POTATO_PACK("potato"),
   SALAD_MIX("salad"),
   SPICE_MIX("seasoned"),
   BOBS_TIN("juicy"),
   BACHS_TIN("rich"),
   BRITTLE_BONES("bone"),
   PUNGENT_ROOT("herb"),
   FRESH_CREAM("cream"),
   PACKAGED_CURRY("decorative"),
   COCONUT_MILK("coconut"),
   SMOKE_POKE_TAIL("smoke_poke"),
   LARGE_LEEK("leek"),
   FANCY_APPLE("apple"),
   BOILED_EGG("egg"),
   FRUIT_BUNCH("tropical"),
   MOOMOO_CHEESE("cheesy"),
   GIGANTAMIX("gigantamix");

   private final ResourceLocation dishTexture;

   private EnumCurryKey(String tex) {
      this.dishTexture = new ResourceLocation(GuiResources.prefix + "items/curry/dish/" + tex + ".png");
   }

   public int getRatingBoost() {
      if (this.ordinal() == 0) {
         return 0;
      } else if (this.ordinal() < 6) {
         return 2;
      } else if (this.ordinal() < 12) {
         return 5;
      } else if (this.ordinal() < 19) {
         return 8;
      } else {
         return this.ordinal() < 25 ? 12 : 20;
      }
   }

   public Item getDishItem() {
      switch (this) {
         case BREAD:
            return PixelmonItems.dishToastCurry;
         case PASTA:
            return PixelmonItems.dishPastaCurry;
         case FRIED_FOOD:
            return PixelmonItems.dishFriedFoodCurry;
         case INSTANT_NOODLES:
            return PixelmonItems.dishInstantNoodleCurry;
         case PRECOOKED_BURGER:
            return PixelmonItems.dishBurgerSteakCurry;
         case SAUSAGES:
            return PixelmonItems.dishSausageCurry;
         case BEAN_TIN:
            return PixelmonItems.dishBeanMedleyCurry;
         case MIXED_MUSHROOMS:
            return PixelmonItems.dishMushroomMedleyCurry;
         case POTATO_PACK:
            return PixelmonItems.dishPotatoCurry;
         case SALAD_MIX:
            return PixelmonItems.dishSaladCurry;
         case SPICE_MIX:
            return PixelmonItems.dishSeasonedCurry;
         case BOBS_TIN:
            return PixelmonItems.dishJuicyCurry;
         case BACHS_TIN:
            return PixelmonItems.dishRichCurry;
         case BRITTLE_BONES:
            return PixelmonItems.dishBoneCurry;
         case PUNGENT_ROOT:
            return PixelmonItems.dishHerbMedleyCurry;
         case FRESH_CREAM:
            return PixelmonItems.dishWhippedCreamCurry;
         case PACKAGED_CURRY:
            return PixelmonItems.dishDecorativeCurry;
         case COCONUT_MILK:
            return PixelmonItems.dishCoconutCurry;
         case SMOKE_POKE_TAIL:
            return PixelmonItems.dishSmokeTailCurry;
         case LARGE_LEEK:
            return PixelmonItems.dishLeekCurry;
         case FANCY_APPLE:
            return PixelmonItems.dishAppleCurry;
         case BOILED_EGG:
            return PixelmonItems.dishBoiledEggCurry;
         case FRUIT_BUNCH:
            return PixelmonItems.dishTropicalCurry;
         case MOOMOO_CHEESE:
            return PixelmonItems.dishCheeseCoveredCurry;
         case GIGANTAMIX:
            return PixelmonItems.dishGigantamixCurry;
         default:
            return PixelmonItems.dishCurry;
      }
   }

   public ResourceLocation getDishTexture() {
      return this.dishTexture;
   }
}

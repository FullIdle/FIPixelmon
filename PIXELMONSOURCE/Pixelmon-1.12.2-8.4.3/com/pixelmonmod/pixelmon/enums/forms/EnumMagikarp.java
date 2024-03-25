package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public enum EnumMagikarp implements IEnumForm, ICosmeticForm {
   NORMAL,
   ROASTED,
   SKELLY,
   CALICO_ORANGE_WHITE,
   CALICO_ORANGE_WHITE_BLACK,
   CALICO_WHITE_ORANGE,
   CALICO_ORANGE_GOLD,
   ORANGE_TWOTONE,
   ORANGE_ORCA,
   ORANGE_DAPPLES,
   PINK_TWOTONE,
   PINK_ORCA,
   PINK_DAPPLES,
   GRAY_BUBBLES,
   GRAY_DIAMONDS,
   GRAY_PATCHES,
   PURPLE_BUBBLES,
   PURPLE_DIAMONDS,
   PURPLE_PATCHES,
   APRICOT_TIGER,
   APRICOT_ZEBRA,
   APRICOT_STRIPES,
   BROWN_TIGER,
   BROWN_ZEBRA,
   BROWN_STRIPES,
   WHITE_FOREHEAD,
   WHITE_MASK,
   BLACK_FOREHEAD,
   BLACK_MASK,
   BLUE_SAUCY,
   BLUE_RAINDROPS,
   VIOLET_SAUCY,
   VIOLET_RAINDROPS;

   public static Pair[] OLD_ROD_WEIGHTS = new Pair[]{ImmutablePair.of(NORMAL, 120), ImmutablePair.of(SKELLY, 11), ImmutablePair.of(CALICO_ORANGE_GOLD, 9), ImmutablePair.of(CALICO_ORANGE_WHITE, 38), ImmutablePair.of(CALICO_ORANGE_WHITE_BLACK, 38), ImmutablePair.of(CALICO_WHITE_ORANGE, 38), ImmutablePair.of(ORANGE_TWOTONE, 40), ImmutablePair.of(ORANGE_ORCA, 10), ImmutablePair.of(ORANGE_DAPPLES, 40), ImmutablePair.of(PINK_TWOTONE, 40), ImmutablePair.of(PINK_ORCA, 10), ImmutablePair.of(PINK_DAPPLES, 40), ImmutablePair.of(GRAY_BUBBLES, 40), ImmutablePair.of(GRAY_DIAMONDS, 40), ImmutablePair.of(GRAY_PATCHES, 10), ImmutablePair.of(PURPLE_BUBBLES, 40), ImmutablePair.of(PURPLE_DIAMONDS, 40), ImmutablePair.of(PURPLE_PATCHES, 10), ImmutablePair.of(APRICOT_TIGER, 11), ImmutablePair.of(APRICOT_ZEBRA, 40), ImmutablePair.of(APRICOT_STRIPES, 40), ImmutablePair.of(BROWN_TIGER, 11), ImmutablePair.of(BROWN_ZEBRA, 40), ImmutablePair.of(BROWN_STRIPES, 40), ImmutablePair.of(WHITE_FOREHEAD, 35), ImmutablePair.of(WHITE_MASK, 8), ImmutablePair.of(BLACK_FOREHEAD, 35), ImmutablePair.of(BLACK_MASK, 8), ImmutablePair.of(BLUE_SAUCY, 32), ImmutablePair.of(BLUE_RAINDROPS, 8), ImmutablePair.of(VIOLET_SAUCY, 32), ImmutablePair.of(VIOLET_RAINDROPS, 8), ImmutablePair.of(EnumFeebas.KARP, 4)};

   public static IEnumForm getWeightedRodForm(int rodQuality) {
      int availableForms = 6;
      switch (rodQuality) {
         case 1:
            availableForms = 9;
            break;
         case 2:
            availableForms = 12;
            break;
         case 3:
            availableForms = 18;
            break;
         case 4:
            availableForms = 24;
            break;
         case 5:
            availableForms = 28;
            break;
         case 6:
            availableForms = 33;
      }

      double totalWeight = 0.0;

      int randomIndex;
      for(randomIndex = 0; randomIndex < availableForms; ++randomIndex) {
         totalWeight += (double)(Integer)OLD_ROD_WEIGHTS[randomIndex].getRight();
      }

      randomIndex = 0;

      for(double random = Math.random() * totalWeight; randomIndex < OLD_ROD_WEIGHTS.length; ++randomIndex) {
         random -= (double)(Integer)OLD_ROD_WEIGHTS[randomIndex].getRight();
         if (random <= 0.0) {
            break;
         }
      }

      return (IEnumForm)OLD_ROD_WEIGHTS[randomIndex].getLeft();
   }

   public String getFormSuffix(boolean shiny) {
      return shiny ? "" : this.getFormSuffix();
   }

   public String getFormSuffix() {
      return this != NORMAL ? "-" + this.name().toLowerCase() : "";
   }

   public String getSpriteSuffix(boolean shiny) {
      if (shiny) {
         return "";
      } else {
         return this != NORMAL ? "-" + this.name().toLowerCase() : "";
      }
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.magikarp.form." + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this != NORMAL;
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return NORMAL;
   }

   public String getName() {
      return this.name();
   }
}

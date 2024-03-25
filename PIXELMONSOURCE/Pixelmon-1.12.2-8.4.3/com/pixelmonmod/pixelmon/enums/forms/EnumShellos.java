package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public enum EnumShellos implements IEnumForm, ICosmeticForm {
   East,
   West,
   Gray_Bubbles,
   Gray_Diamonds,
   Purple_Bubbles,
   Purple_Diamonds,
   Violet_Saucy,
   Brown_Stripes,
   Apricot_Saucy,
   Blue_Stripes,
   Moons,
   Sun,
   GoldE,
   GoldW;

   public static Pair[] OLD_ROD_WEIGHTS = new Pair[]{ImmutablePair.of(East, 120), ImmutablePair.of(West, 120), ImmutablePair.of(Gray_Bubbles, 60), ImmutablePair.of(Purple_Bubbles, 60), ImmutablePair.of(Gray_Diamonds, 60), ImmutablePair.of(Purple_Diamonds, 60), ImmutablePair.of(Violet_Saucy, 40), ImmutablePair.of(Apricot_Saucy, 40), ImmutablePair.of(Brown_Stripes, 40), ImmutablePair.of(Blue_Stripes, 40), ImmutablePair.of(Moons, 20), ImmutablePair.of(Sun, 20), ImmutablePair.of(GoldE, 5), ImmutablePair.of(GoldW, 5)};

   public static EnumShellos getWeightedRodForm(int rodQuality) {
      int availableForms = 2;
      switch (rodQuality) {
         case 1:
            availableForms = 4;
            break;
         case 2:
            availableForms = 6;
            break;
         case 3:
            availableForms = 8;
            break;
         case 4:
            availableForms = 10;
            break;
         case 5:
            availableForms = 12;
            break;
         case 6:
            availableForms = 14;
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

      return (EnumShellos)OLD_ROD_WEIGHTS[randomIndex].getLeft();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == East || this == West;
   }

   public String getUnlocalizedName() {
      return "pixelmon.shellos.form." + this.name().toLowerCase();
   }

   public static EnumShellos getFromIndex(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return East;
      }
   }

   public String getFormSuffix(boolean shiny) {
      return shiny ? "-" + (this.ordinal() % 2 == 0 ? East.name().toLowerCase() : West.name().toLowerCase()) : this.getFormSuffix();
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this != East && this != West;
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return this.ordinal() % 2 == 0 ? East : West;
   }

   public String getName() {
      return this.name();
   }
}

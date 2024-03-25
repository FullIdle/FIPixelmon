package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public enum EnumBurningSalt implements IEnumForm, ICosmeticForm {
   NORMAL,
   COPPER_SULFATE,
   CUPRIC_CHLORIDE,
   LITHIUM_CHLORIDE,
   MAGNESIUM_SULFIDE,
   MANGANESE_CHLORIDE,
   POTASSIUM_CHLORIDE,
   SODIUM_CARBONATE,
   CALCIUM_CARBONATE;

   public static Pair[] OLD_ROD_WEIGHTS = new Pair[]{ImmutablePair.of(NORMAL, 120), ImmutablePair.of(COPPER_SULFATE, 60), ImmutablePair.of(CUPRIC_CHLORIDE, 60), ImmutablePair.of(LITHIUM_CHLORIDE, 60), ImmutablePair.of(MAGNESIUM_SULFIDE, 40), ImmutablePair.of(MANGANESE_CHLORIDE, 40), ImmutablePair.of(POTASSIUM_CHLORIDE, 20), ImmutablePair.of(SODIUM_CARBONATE, 15), ImmutablePair.of(CALCIUM_CARBONATE, 5)};

   public static EnumBurningSalt getWeightedRodForm(int rodQuality) {
      int availableForms = 1;
      switch (rodQuality) {
         case 1:
            availableForms = 2;
            break;
         case 2:
            availableForms = 4;
            break;
         case 3:
            availableForms = 5;
            break;
         case 4:
            availableForms = 6;
            break;
         case 5:
            availableForms = 8;
            break;
         case 6:
            availableForms = 9;
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

      return (EnumBurningSalt)OLD_ROD_WEIGHTS[randomIndex].getLeft();
   }

   public static EnumBurningSalt getFromIndex(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return NORMAL;
      }
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.burning_salt.form." + this.name().toLowerCase();
   }

   public String getFormSuffix(boolean shiny) {
      return shiny ? "" : this.getFormSuffix();
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this != NORMAL;
   }

   public Set getFormAttributes() {
      return (Set)(this != NORMAL ? Sets.immutableEnumSet(FormAttributes.COSMETIC, new FormAttributes[0]) : IEnumForm.super.getFormAttributes());
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return NORMAL;
   }

   public String getName() {
      return this.name();
   }
}

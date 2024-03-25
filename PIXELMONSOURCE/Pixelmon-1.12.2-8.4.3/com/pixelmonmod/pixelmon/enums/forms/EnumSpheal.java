package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public enum EnumSpheal implements IEnumForm, ICosmeticForm {
   NORMAL,
   POKEBALL,
   DUSKBALL,
   LOVEBALL,
   FRIENDBALL,
   HEAVYBALL,
   MOONBALL,
   PREMIERBALL,
   MASTERBALL,
   GSBALL;

   public static Pair[] OLD_ROD_WEIGHTS = new Pair[]{ImmutablePair.of(NORMAL, 120), ImmutablePair.of(POKEBALL, 60), ImmutablePair.of(DUSKBALL, 60), ImmutablePair.of(LOVEBALL, 60), ImmutablePair.of(FRIENDBALL, 40), ImmutablePair.of(HEAVYBALL, 40), ImmutablePair.of(MOONBALL, 20), ImmutablePair.of(PREMIERBALL, 15), ImmutablePair.of(MASTERBALL, 5), ImmutablePair.of(GSBALL, 1)};

   public static EnumSpheal getWeightedRodForm(int rodQuality) {
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
            availableForms = 10;
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

      return (EnumSpheal)OLD_ROD_WEIGHTS[randomIndex].getLeft();
   }

   public static EnumSpheal getFromIndex(int index) {
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
      return "pixelmon.spheal.form." + this.name().toLowerCase();
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

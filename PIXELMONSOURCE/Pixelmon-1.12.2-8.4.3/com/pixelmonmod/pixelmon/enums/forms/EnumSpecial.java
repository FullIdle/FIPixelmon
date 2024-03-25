package com.pixelmonmod.pixelmon.enums.forms;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.Set;
import javax.annotation.Nonnull;

public enum EnumSpecial implements IEnumForm, ICosmeticForm {
   /** @deprecated */
   @Deprecated
   Base("", (byte)0),
   Zombie("-zombie", (byte)100),
   Online("-online", (byte)101),
   Drowned("-drowned", (byte)102),
   Valentine("-valentine", (byte)103),
   Rainbow("-rainbow", (byte)104),
   Alien("-alien", (byte)105),
   Valencian("-valencian", (byte)106),
   Alter("-alter", (byte)107),
   Pink("-pink", (byte)108),
   Summer("-summer", (byte)109),
   Crystal("-crystal", (byte)110),
   Creator("-creator", (byte)111),
   Strike("-strike", (byte)112),
   Ashen("-ashen", (byte)113),
   Spirit("-spirit", (byte)114),
   Halloween("-halloween", (byte)115);

   private String suffix;
   private byte index;

   private EnumSpecial(String suffix, byte index) {
      this.suffix = suffix;
      this.index = index;
   }

   public String getFormSuffix(boolean shiny) {
      return shiny && this != Rainbow && this != Valentine ? "" : this.getFormSuffix();
   }

   public String getFormSuffix() {
      return this.suffix;
   }

   public byte getForm() {
      return this.index;
   }

   public boolean isDefaultForm() {
      return false;
   }

   public String getUnlocalizedName() {
      return "pixelmon.generic.form." + this.name().toLowerCase();
   }

   public boolean hasShiny(EnumSpecies species) {
      if (this != Rainbow && this != Valentine) {
         return false;
      } else {
         return species != EnumSpecies.Skarmory;
      }
   }

   public boolean isCosmetic() {
      return true;
   }

   public Set getFormAttributes() {
      return (Set)(this != Base ? Sets.immutableEnumSet(FormAttributes.COSMETIC, new FormAttributes[0]) : IEnumForm.super.getFormAttributes());
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return (IEnumForm)(EnumSpecies.mfTextured.contains(pokemon.getSpecies()) ? pokemon.getGender() : (IEnumForm)pokemon.getSpecies().getPossibleForms(false).get(0));
   }

   public String getName() {
      return this.name();
   }
}

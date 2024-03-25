package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public enum EnumXerneas implements IEnumForm, ICosmeticForm {
   NEUTRAL,
   ACTIVE,
   NEUTRAL_CREATOR,
   ACTIVE_CREATOR;

   public String getSpriteSuffix(boolean shiny) {
      return this == NEUTRAL_CREATOR ? NEUTRAL.getFormSuffix() : (this == ACTIVE_CREATOR ? ACTIVE.getFormSuffix() : this.getFormSuffix());
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase().replace("_", "-");
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == ACTIVE || this == ACTIVE_CREATOR;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return pokemon.getFormEnum() == ACTIVE_CREATOR ? NEUTRAL_CREATOR : NEUTRAL;
   }

   public IEnumForm getDefaultFromForm(IEnumForm form) {
      return ACTIVE;
   }

   public boolean isDefaultForm() {
      return this == NEUTRAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.xerneas.form." + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this == NEUTRAL_CREATOR || this == ACTIVE_CREATOR;
   }

   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return (IEnumForm)(pokemon.getFormEnum() == NEUTRAL_CREATOR ? NEUTRAL : (pokemon.getFormEnum() == ACTIVE_CREATOR ? ACTIVE : pokemon.getFormEnum()));
   }

   public boolean hasShiny(EnumSpecies species) {
      return false;
   }

   public String getName() {
      return this.name();
   }
}

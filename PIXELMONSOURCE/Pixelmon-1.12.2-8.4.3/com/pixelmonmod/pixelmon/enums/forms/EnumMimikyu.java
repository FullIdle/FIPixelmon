package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import javax.annotation.Nonnull;

public enum EnumMimikyu implements IEnumForm, ICosmeticForm {
   Disguised,
   Busted,
   Disguised_Spirit,
   Busted_Spirit;

   public String getFormSuffix() {
      return this != Disguised && this != Busted ? "-spirit" : "";
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == Busted || this == Busted_Spirit;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return pokemon.getFormEnum() == Busted ? Disguised : (pokemon.getFormEnum() == Busted_Spirit ? Disguised_Spirit : Disguised);
   }

   public IEnumForm getDefaultFromForm(IEnumForm form) {
      return Disguised;
   }

   public boolean isDefaultForm() {
      return this == Disguised;
   }

   public String getUnlocalizedName() {
      return "pixelmon.mimikyu.form." + this.name().toLowerCase();
   }

   public boolean isCosmetic() {
      return this != Disguised && this != Busted;
   }

   @Nonnull
   public IEnumForm getBaseFromCosmetic(Pokemon pokemon) {
      return (IEnumForm)(pokemon.getFormEnum() == Disguised_Spirit ? Disguised : (pokemon.getFormEnum() == Busted_Spirit ? Busted : pokemon.getFormEnum()));
   }

   public String getName() {
      return this.name();
   }
}

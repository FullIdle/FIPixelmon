package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public enum EnumMeloetta implements IEnumForm {
   ARIA,
   PIROUETTE;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == PIROUETTE;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return ARIA;
   }

   public boolean isDefaultForm() {
      return this == ARIA;
   }

   public String getUnlocalizedName() {
      return "pixelmon.meloetta.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

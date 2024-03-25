package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public enum EnumHeroDuo implements IEnumForm {
   HERO,
   CROWNED;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return !this.isDefaultForm();
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return HERO;
   }

   public boolean isDefaultForm() {
      return this == HERO;
   }

   public String getUnlocalizedName() {
      return "pixelmon.hero_duo.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

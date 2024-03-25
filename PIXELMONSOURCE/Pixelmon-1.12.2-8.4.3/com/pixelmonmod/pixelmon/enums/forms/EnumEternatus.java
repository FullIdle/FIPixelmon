package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public enum EnumEternatus implements IEnumForm {
   ORDINARY,
   ETERNAMAX,
   ETERNAMAXCOSMETIC;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == ORDINARY;
   }

   public boolean isTemporary() {
      return this == ETERNAMAX;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return ORDINARY;
   }

   public String getUnlocalizedName() {
      return "pixelmon.eternatus.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

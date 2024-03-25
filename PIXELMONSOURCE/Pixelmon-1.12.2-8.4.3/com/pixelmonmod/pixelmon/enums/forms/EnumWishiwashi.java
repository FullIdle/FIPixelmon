package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public enum EnumWishiwashi implements IEnumForm {
   SOLO,
   SCHOOL;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == SCHOOL;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return SOLO;
   }

   public boolean isDefaultForm() {
      return this == SOLO;
   }

   public String getUnlocalizedName() {
      return "pixelmon.wishiwashi.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

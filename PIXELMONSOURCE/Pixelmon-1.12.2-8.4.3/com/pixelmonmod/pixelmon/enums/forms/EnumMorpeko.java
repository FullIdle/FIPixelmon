package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public enum EnumMorpeko implements IEnumForm {
   FULLBELLY,
   HANGRY;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return this == HANGRY;
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return FULLBELLY;
   }

   public boolean isDefaultForm() {
      return this == FULLBELLY;
   }

   public String getUnlocalizedName() {
      return "pixelmon.morpeko.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

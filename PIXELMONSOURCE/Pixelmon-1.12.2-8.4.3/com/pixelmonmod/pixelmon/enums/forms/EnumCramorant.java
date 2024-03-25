package com.pixelmonmod.pixelmon.enums.forms;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public enum EnumCramorant implements IEnumForm {
   NORMAL,
   GULPING,
   GORGING;

   public String getFormSuffix() {
      return "";
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isTemporary() {
      return !this.isDefaultForm();
   }

   public IEnumForm getDefaultFromTemporary(Pokemon pokemon) {
      return NORMAL;
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.cramorant.form." + this.name().toLowerCase();
   }

   public String getSpriteSuffix(boolean shiny) {
      return NORMAL.getFormSuffix(shiny);
   }

   public String getName() {
      return this.name();
   }
}

package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumAuthentic implements IEnumForm {
   Phony,
   Antique;

   public String getSpriteSuffix(boolean shiny) {
      return "";
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return true;
   }

   public String getUnlocalizedName() {
      return "pixelmon.authentic.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

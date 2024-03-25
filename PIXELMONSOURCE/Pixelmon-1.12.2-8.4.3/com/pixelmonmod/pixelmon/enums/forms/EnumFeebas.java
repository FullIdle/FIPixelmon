package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumFeebas implements IEnumForm {
   NORMAL,
   KARP;

   public String getFormSuffix(boolean shiny) {
      return shiny ? "" : this.getFormSuffix();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.feebas.form." + this.name().toLowerCase();
   }

   public String getFormSuffix() {
      return this == NORMAL ? "" : "-" + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

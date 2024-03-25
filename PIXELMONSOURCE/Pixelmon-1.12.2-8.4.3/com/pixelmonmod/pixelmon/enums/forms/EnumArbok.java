package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumArbok implements IEnumForm {
   NORMAL,
   MAIN_CIRCULAR,
   SECONDARY_CIRCULAR,
   DARK;

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
      return "pixelmon.arbok.form." + this.name().toLowerCase();
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

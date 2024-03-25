package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumSolgaleo implements IEnumForm {
   Normal,
   Real;

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == Normal;
   }

   public String getUnlocalizedName() {
      return "pixelmon.solgaleo.form." + this.name().toLowerCase();
   }

   public String getFormSuffix(boolean shiny) {
      return shiny && this == Real ? "-normal" : this.getFormSuffix();
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

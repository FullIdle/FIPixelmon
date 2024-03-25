package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumLunatone implements IEnumForm {
   GIBBOUS,
   QUARTER,
   FULL,
   NEW_MOON,
   CRESCENT;

   public String getFormSuffix() {
      switch (this) {
         case GIBBOUS:
         case QUARTER:
         case FULL:
         case CRESCENT:
         default:
            return "";
         case NEW_MOON:
            return "-2";
      }
   }

   public String getSpriteSuffix(boolean shiny) {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.lunatone.form." + this.name().toLowerCase();
   }

   public boolean isDefaultForm() {
      return this == GIBBOUS;
   }

   public String getName() {
      return this.name();
   }
}

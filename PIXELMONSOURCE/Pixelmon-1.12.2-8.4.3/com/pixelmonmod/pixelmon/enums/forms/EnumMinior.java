package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumMinior implements IEnumForm {
   METEOR,
   RED,
   ORANGE,
   YELLOW,
   GREEN,
   BLUE,
   INDIGO,
   VIOLET;

   public String getFormSuffix(boolean shiny) {
      if (shiny) {
         return this == METEOR ? "-meteor" : "-core";
      } else {
         return this.getFormSuffix();
      }
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.minior.form." + this.name().toLowerCase();
   }

   public boolean isDefaultForm() {
      return this == METEOR;
   }

   public String getName() {
      return this.name();
   }
}

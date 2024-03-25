package com.pixelmonmod.pixelmon.enums.forms;

public enum SeasonForm implements IEnumForm {
   Spring,
   Summer,
   Autumn,
   Winter;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.season.form." + this.name().toLowerCase();
   }

   public static SeasonForm getFromIndex(int index) {
      if (index == -1) {
         return Winter;
      } else {
         try {
            return values()[index];
         } catch (IndexOutOfBoundsException var2) {
            return Winter;
         }
      }
   }

   public String getName() {
      return this.name();
   }
}

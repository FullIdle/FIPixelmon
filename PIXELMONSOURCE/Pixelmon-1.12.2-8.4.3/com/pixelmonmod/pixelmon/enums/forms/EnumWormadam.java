package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumWormadam implements IEnumForm {
   Plant,
   Sandy,
   Trash;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.wormadam.form." + this.name().toLowerCase();
   }

   public static EnumWormadam getFromIndex(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return Plant;
      }
   }

   public String getName() {
      return this.name();
   }
}

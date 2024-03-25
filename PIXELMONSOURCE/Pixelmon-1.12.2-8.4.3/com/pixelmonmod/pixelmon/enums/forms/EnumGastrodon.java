package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumGastrodon implements IEnumForm {
   East,
   West;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.gastrodon.form." + this.name().toLowerCase();
   }

   public static EnumGastrodon getFromIndex(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return East;
      }
   }

   public String getName() {
      return this.name();
   }
}

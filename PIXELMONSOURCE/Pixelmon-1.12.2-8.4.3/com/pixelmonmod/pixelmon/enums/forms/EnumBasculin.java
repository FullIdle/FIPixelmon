package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumBasculin implements IEnumForm {
   RED,
   BLUE,
   WHITE;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.basculin.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

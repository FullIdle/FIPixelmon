package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumFlabebe implements IEnumForm {
   RED,
   YELLOW,
   ORANGE,
   BLUE,
   WHITE,
   AZ;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.flabebe.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

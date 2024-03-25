package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumShaymin implements IEnumForm {
   LAND,
   SKY;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == LAND;
   }

   public String getUnlocalizedName() {
      return "pixelmon.shaymin.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

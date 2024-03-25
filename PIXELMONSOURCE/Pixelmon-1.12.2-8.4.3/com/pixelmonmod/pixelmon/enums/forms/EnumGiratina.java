package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumGiratina implements IEnumForm {
   ALTERED,
   ORIGIN;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == ALTERED;
   }

   public String getUnlocalizedName() {
      return "pixelmon.giratina.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumOricorio implements IEnumForm {
   BAILE,
   POMPOM,
   PAU,
   SENSU;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.oricorio.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

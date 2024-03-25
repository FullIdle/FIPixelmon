package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumNoForm implements IEnumForm {
   NoForm;

   public String getFormSuffix() {
      return "";
   }

   public byte getForm() {
      return -1;
   }

   public boolean isDefaultForm() {
      return true;
   }

   public String getUnlocalizedName() {
      return "pixelmon.generic.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumTherian implements IEnumForm {
   INCARNATE,
   THERIAN;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == INCARNATE;
   }

   public String getUnlocalizedName() {
      return "pixelmon.therian.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

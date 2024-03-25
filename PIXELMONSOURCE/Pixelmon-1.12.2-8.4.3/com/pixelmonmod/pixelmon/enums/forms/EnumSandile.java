package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumSandile implements IEnumForm {
   NORMAL,
   BLOCKY;

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.sandile.form." + this.name().toLowerCase();
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

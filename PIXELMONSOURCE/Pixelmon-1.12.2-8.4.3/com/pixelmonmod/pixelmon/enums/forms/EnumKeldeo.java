package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumKeldeo implements IEnumForm {
   ORDINARY,
   RESOLUTE;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == ORDINARY;
   }

   public String getUnlocalizedName() {
      return "pixelmon.keldeo.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

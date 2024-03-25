package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumCherrim implements IEnumForm {
   OVERCAST,
   SUNSHINE;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == OVERCAST;
   }

   public String getUnlocalizedName() {
      return "pixelmon.cherrim.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

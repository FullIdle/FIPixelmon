package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumGenesect implements IEnumForm {
   NORMAL,
   BURN,
   CHILL,
   DOUSE,
   SHOCK;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.genesect.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

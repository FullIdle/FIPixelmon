package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumLycanroc implements IEnumForm {
   MIDDAY,
   MIDNIGHT,
   DUSK;

   public boolean isDefaultForm() {
      return this != DUSK;
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.lycanroc.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

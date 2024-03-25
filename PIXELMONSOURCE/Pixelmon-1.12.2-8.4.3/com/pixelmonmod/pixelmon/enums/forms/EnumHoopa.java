package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumHoopa implements IEnumForm {
   CONFINED,
   UNBOUND;

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == CONFINED;
   }

   public String getUnlocalizedName() {
      return "pixelmon.hoopa.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

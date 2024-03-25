package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumKyurem implements IEnumForm {
   NORMAL,
   BLACK,
   WHITE;

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.kyurem.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

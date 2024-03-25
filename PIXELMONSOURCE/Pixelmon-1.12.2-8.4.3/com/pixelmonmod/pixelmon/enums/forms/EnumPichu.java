package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumPichu implements IEnumForm {
   NORMAL,
   SPIKY;

   public String getFormSuffix() {
      return "";
   }

   public String getSpriteSuffix(boolean shiny) {
      return this == NORMAL ? "" : "-spiky";
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.pichu.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

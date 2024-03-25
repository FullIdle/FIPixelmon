package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumAmoonguss implements IEnumForm {
   NORMAL,
   BLACK,
   BROWN,
   CYAN,
   DARK_BLUE,
   DARK_GREEN,
   DARK_PINK,
   DARK_RED,
   FORTE_GREEN,
   GREY,
   LIGHT_BROWN,
   LIGHT_PINK,
   LIGHT_YELLOW,
   LIME,
   OLIVE,
   ORANGE,
   PINK,
   PURPLE,
   RED,
   WHITE,
   YELLOW;

   public String getFormSuffix(boolean shiny) {
      return shiny ? "" : this.getFormSuffix();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public boolean isDefaultForm() {
      return this == NORMAL;
   }

   public String getUnlocalizedName() {
      return "pixelmon.amoonguss.form." + this.name().toLowerCase();
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

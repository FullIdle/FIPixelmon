package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumGroudon implements IEnumForm {
   Meta(3);

   private final byte form;

   private EnumGroudon(int form) {
      this.form = (byte)form;
   }

   public String getFormSuffix(boolean shiny) {
      return shiny ? "-normal" : this.getFormSuffix();
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return this.form;
   }

   public String getUnlocalizedName() {
      return "pixelmon.groudon.form." + this.name().toLowerCase();
   }

   public String getName() {
      return this.name();
   }
}

package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumCastform implements IEnumForm {
   Normal,
   Ice("Snowy"),
   Rain("Rainy"),
   Sun("Sunny");

   private String altName;

   private EnumCastform() {
      this.altName = this.name();
   }

   private EnumCastform(String altName) {
      this.altName = altName;
   }

   public String getFormSuffix() {
      return "-" + this.name().toLowerCase();
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.castform.form." + this.name().toLowerCase();
   }

   public static EnumCastform getFromIndex(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return Normal;
      }
   }

   public static EnumCastform getFromName(String name) {
      EnumCastform[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumCastform form = var1[var3];
         if (form.name().equals(name) || form.altName.equals(name)) {
            return form;
         }
      }

      return null;
   }

   public boolean isDefaultForm() {
      return this == Normal;
   }

   public String getName() {
      return this.name();
   }
}

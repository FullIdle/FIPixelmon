package com.pixelmonmod.pixelmon.enums.forms;

public enum EnumUnown implements IEnumForm {
   A,
   B,
   C,
   D,
   E,
   F,
   G,
   H,
   I,
   J,
   K,
   L,
   M,
   N,
   O,
   P,
   Q,
   R,
   S,
   T,
   U,
   V,
   W,
   X,
   Y,
   Z,
   Question('?'),
   Exclamation('!');

   private char altChar;

   private EnumUnown(char altName) {
      this.altChar = altName;
   }

   private EnumUnown() {
      this.altChar = this.name().charAt(0);
   }

   public char getCharacter() {
      return this.altChar;
   }

   public String getSpriteSuffix(boolean shiny) {
      return "-" + this.name().toLowerCase();
   }

   public String getFormSuffix() {
      return "";
   }

   public byte getForm() {
      return (byte)this.ordinal();
   }

   public String getUnlocalizedName() {
      return "pixelmon.unown.form." + this.name().toLowerCase();
   }

   public static EnumUnown getFromIndex(int index) {
      try {
         return values()[index];
      } catch (IndexOutOfBoundsException var2) {
         return A;
      }
   }

   public static EnumUnown getFromName(String name) {
      EnumUnown[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumUnown form = var1[var3];
         if (form.name().equals(name) || (form.altChar + "").equals(name)) {
            return form;
         }
      }

      return null;
   }

   public String getName() {
      return this.name();
   }
}

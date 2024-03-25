package com.pixelmonmod.tcg.gui.enums;

public enum EnumGui {
   Deck,
   Printer,
   Duel,
   Binder,
   Disenchanter;

   public int getIndex() {
      return this.ordinal();
   }

   public static EnumGui fromIndex(int index) {
      return values()[index];
   }
}

package com.pixelmonmod.pixelmon.enums.items;

public enum EnumBadgeCase {
   Black,
   White,
   Pink,
   Green,
   Blue,
   Yellow,
   Red;

   public static EnumBadgeCase fromIndex(int index) {
      return index >= 0 && index < values().length ? values()[index] : null;
   }
}

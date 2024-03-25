package com.pixelmonmod.pixelmon.enums;

public enum TextJustification {
   RIGHT,
   CENTER,
   LEFT;

   public static TextJustification get(short value) {
      if (value > 0) {
         return LEFT;
      } else {
         return value < 0 ? RIGHT : CENTER;
      }
   }
}

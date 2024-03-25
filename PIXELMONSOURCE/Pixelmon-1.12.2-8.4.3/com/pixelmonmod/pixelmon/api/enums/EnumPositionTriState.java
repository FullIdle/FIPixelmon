package com.pixelmonmod.pixelmon.api.enums;

public enum EnumPositionTriState {
   LEFT,
   RIGHT,
   CENTER;

   public static EnumPositionTriState[] naturalOrder() {
      return new EnumPositionTriState[]{LEFT, CENTER, RIGHT};
   }
}

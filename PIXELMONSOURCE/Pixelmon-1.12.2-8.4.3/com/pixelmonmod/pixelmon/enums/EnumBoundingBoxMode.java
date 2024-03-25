package com.pixelmonmod.pixelmon.enums;

public enum EnumBoundingBoxMode {
   None,
   Pushout,
   Solid;

   public static EnumBoundingBoxMode getMode(byte mode) {
      return mode == 1 ? Pushout : (mode == 2 ? Solid : None);
   }
}

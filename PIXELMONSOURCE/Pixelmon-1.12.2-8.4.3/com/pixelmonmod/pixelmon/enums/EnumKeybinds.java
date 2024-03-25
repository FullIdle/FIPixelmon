package com.pixelmonmod.pixelmon.enums;

public enum EnumKeybinds {
   Jump,
   Crouch,
   Back,
   Forward,
   Left,
   Right,
   Sprint,
   Attack;

   public static EnumKeybinds getMovement(int index) {
      try {
         return values()[index];
      } catch (Exception var2) {
         return null;
      }
   }

   public static boolean hasMovement(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public boolean breaksHover() {
      return this == Forward || this == Back;
   }
}

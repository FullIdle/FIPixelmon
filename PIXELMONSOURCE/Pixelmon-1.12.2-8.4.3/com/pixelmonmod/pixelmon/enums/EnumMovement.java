package com.pixelmonmod.pixelmon.enums;

public enum EnumMovement {
   Jump,
   Descend,
   Crouch,
   Back,
   Forward,
   Left,
   Right;

   public static EnumMovement getMovement(int index) {
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
}

package com.pixelmonmod.pixelmon.enums;

public enum EnumGuiContainer {
   MechanicalAnvil,
   Infuser,
   CookingPot;

   public int getIndex() {
      return this.ordinal();
   }

   public static boolean hasGUI(String name) {
      try {
         return EnumGuiScreen.valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public static EnumGuiContainer getFromOrdinal(int id) {
      return id >= 0 && id < values().length ? values()[id] : null;
   }
}

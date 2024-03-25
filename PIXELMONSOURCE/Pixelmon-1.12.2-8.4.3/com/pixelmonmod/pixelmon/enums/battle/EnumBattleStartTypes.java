package com.pixelmonmod.pixelmon.enums.battle;

public enum EnumBattleStartTypes {
   PUGRASSSINGLE,
   PUGRASSDOUBLE,
   SEAWEED,
   ROCKSMASH,
   DIVE,
   HEADBUTT,
   SWEETSCENT,
   CURRY,
   CAVEROCK,
   FORAGE;

   public static String[] getTypeNames() {
      String[] typeNames = new String[values().length];

      for(int i = 0; i < values().length; ++i) {
         typeNames[i] = values()[i].name();
      }

      return typeNames;
   }
}

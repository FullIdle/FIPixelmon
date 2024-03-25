package com.pixelmonmod.pixelmon.battles.attacks;

public enum DamageTypeEnum {
   ATTACK,
   ATTACKFIXED,
   STATUS,
   ABILITY,
   SUBSTITUTE,
   RECOIL,
   SELF,
   CRASH,
   STRUGGLE,
   WEATHER,
   ITEM;

   public boolean isDirect() {
      return this == ATTACK || this == ATTACKFIXED;
   }
}

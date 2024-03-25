package com.pixelmonmod.pixelmon.battles.controller.log;

import java.util.Locale;

public enum AttackResult {
   proceed,
   hit,
   ignore,
   killed,
   succeeded,
   charging,
   unable,
   failed,
   missed,
   notarget;

   public boolean isSuccess() {
      return this == hit || this == succeeded || this == killed;
   }

   public static AttackResult parseOrNull(String name) {
      try {
         return valueOf(name.toLowerCase(Locale.ROOT));
      } catch (Exception var2) {
         return null;
      }
   }
}

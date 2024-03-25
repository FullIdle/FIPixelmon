package com.pixelmonmod.pixelmon.enums.items;

import java.util.Locale;

public enum EnumSymbols {
   Gold_Knowledge,
   Gold_Guts,
   Gold_Tactics,
   Gold_Luck,
   Gold_Spirits,
   Gold_Brave,
   Gold_Ability,
   Silver_Knowledge,
   Silver_Guts,
   Silver_Tactics,
   Silver_Luck,
   Silver_Spirits,
   Silver_Brave,
   Silver_Ability;

   public String getFileName() {
      return this.toString().toLowerCase(Locale.US) + "_symbol";
   }
}

package com.pixelmonmod.pixelmon.blocks.enums;

import net.minecraft.util.text.translation.I18n;

public enum EnumSpawnerAggression {
   Default,
   Timid,
   Passive,
   Aggressive;

   public static EnumSpawnerAggression getFromOrdinal(int ordinal) {
      try {
         return values()[ordinal];
      } catch (IndexOutOfBoundsException var2) {
         return Default;
      }
   }

   public static EnumSpawnerAggression nextAggression(EnumSpawnerAggression aggression) {
      return getFromOrdinal((aggression.ordinal() + 1) % values().length);
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("enum.aggro." + this.toString().toLowerCase());
   }
}

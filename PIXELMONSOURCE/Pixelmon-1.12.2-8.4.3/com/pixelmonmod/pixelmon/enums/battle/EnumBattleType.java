package com.pixelmonmod.pixelmon.enums.battle;

import net.minecraft.util.text.translation.I18n;

public enum EnumBattleType {
   Single(1),
   Double(2),
   Triple(3),
   Rotation(3),
   Raid(4),
   Horde(5);

   public int numPokemon;

   private EnumBattleType(int numPokemon) {
      this.numPokemon = numPokemon;
   }

   public boolean areSlotsLocked() {
      return this == Raid || this == Horde;
   }

   public EnumBattleType next() {
      return this == Horde ? Single : values()[this.ordinal() + 1];
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("gui.acceptdeny." + this.toString().toLowerCase());
   }

   public static EnumBattleType getFromOrdinal(int ordinal) {
      EnumBattleType[] values = values();
      return ordinal >= 0 && ordinal < values.length ? values[ordinal] : Single;
   }
}

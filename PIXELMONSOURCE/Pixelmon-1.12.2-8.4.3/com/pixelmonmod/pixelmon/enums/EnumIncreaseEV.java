package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public enum EnumIncreaseEV {
   HpUp(StatsType.HP, "hpup", false),
   Protein(StatsType.Attack, "protein", false),
   Iron(StatsType.Defence, "iron", false),
   Calcium(StatsType.SpecialAttack, "calcium", false),
   Zinc(StatsType.SpecialDefence, "zinc", false),
   Carbos(StatsType.Speed, "carbos", false),
   HealthFeather(StatsType.HP, "health_feather", true),
   MuscleFeather(StatsType.Attack, "muscle_feather", true),
   ResistFeather(StatsType.Defence, "resist_feather", true),
   GeniusFeather(StatsType.SpecialAttack, "genius_feather", true),
   CleverFeather(StatsType.SpecialDefence, "clever_feather", true),
   SwiftFeather(StatsType.Speed, "swift_feather", true);

   public StatsType statAffected;
   public String textureName;
   public boolean isFeather;

   private EnumIncreaseEV(StatsType statAffected, String textureName, boolean isFeather) {
      this.statAffected = statAffected;
      this.textureName = textureName;
      this.isFeather = isFeather;
   }

   public static boolean hasIncreaseEV(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}

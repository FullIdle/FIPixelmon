package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public enum EnumDecreaseEV {
   PomegBerry(StatsType.HP, "pomegberry"),
   KelpsyBerry(StatsType.Attack, "kelpsyberry"),
   QualotBerry(StatsType.Defence, "qualotberry"),
   HondewBerry(StatsType.SpecialAttack, "hondewberry"),
   GrepaBerry(StatsType.SpecialDefence, "grepaberry"),
   TamatoBerry(StatsType.Speed, "tamatoberry");

   public String textureName;
   public StatsType statAffected;

   private EnumDecreaseEV(StatsType statAffected, String textureName) {
      this.statAffected = statAffected;
      this.textureName = textureName;
   }

   public static boolean hasDecreaseEV(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}

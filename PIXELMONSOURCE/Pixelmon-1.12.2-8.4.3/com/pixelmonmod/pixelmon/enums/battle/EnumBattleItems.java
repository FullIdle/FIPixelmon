package com.pixelmonmod.pixelmon.enums.battle;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public enum EnumBattleItems {
   xAttack(StatsType.Attack, "xattack"),
   xDefence(StatsType.Defence, "xdefence"),
   xSpecialAttack(StatsType.SpecialAttack, "xspecialattack"),
   xSpecialDefence(StatsType.SpecialDefence, "xspecialdefence"),
   xSpeed(StatsType.Speed, "xspeed"),
   xAccuracy(StatsType.Accuracy, "xaccuracy"),
   direHit(StatsType.None, "direhit"),
   guardSpec(StatsType.None, "guardspec"),
   maxMushroom(StatsType.None, "maxmushroom"),
   fluffyTail(StatsType.None, "fluffytail"),
   redFlute(StatsType.None, "red_flute"),
   greenFlute(StatsType.None, "green_flute"),
   blueFlute(StatsType.None, "blue_flute"),
   yellowFlute(StatsType.None, "yellow_flute");

   public StatsType effectType;
   public String textureName;
   private boolean affectsBattle;
   private boolean usableInBattle;

   private EnumBattleItems(StatsType type, String textureName) {
      this.effectType = type;
      this.textureName = textureName;
   }

   private EnumBattleItems() {
      this(true, true);
   }

   private EnumBattleItems(boolean usableInBattle, boolean affectsBattle) {
      this.usableInBattle = usableInBattle;
      this.affectsBattle = affectsBattle;
   }

   public boolean getUsableInBattle() {
      return this.usableInBattle;
   }

   public boolean getAffectsBattle() {
      return this.affectsBattle;
   }

   public static boolean hasBattleItem(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}

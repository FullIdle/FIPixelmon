package com.pixelmonmod.pixelmon.sounds;

public enum BattleMusicType {
   WILD,
   PLAYER,
   TRAINER,
   GYM,
   BOSS,
   LEGENDARY,
   RAID,
   CUSTOM;

   private static final BattleMusicType[] VALUES = values();

   public static BattleMusicType getFromIndex(int index) {
      return index > 0 && index < VALUES.length ? VALUES[index] : WILD;
   }
}

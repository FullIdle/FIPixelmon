package com.pixelmonmod.tcg.duel.attack.enums;

import com.pixelmonmod.pixelmon.RandomHelper;

public enum CoinSide {
   Head,
   Tail;

   public static CoinSide getRandom() {
      return RandomHelper.getRandomChance(0.5) ? Head : Tail;
   }
}

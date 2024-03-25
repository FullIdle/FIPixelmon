package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class MoveCondition extends EvoCondition {
   public int attackIndex;

   public MoveCondition() {
      super("move");
      this.attackIndex = 1;
   }

   public MoveCondition(int attackIndex) {
      this();
      this.attackIndex = attackIndex;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      Attack[] var2 = pixelmon.getPokemonData().getMoveset().attacks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Attack move = var2[var4];
         if (move != null && move.getActualMove().getAttackId() == this.attackIndex) {
            return true;
         }
      }

      return false;
   }
}

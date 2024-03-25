package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class MoveTypeCondition extends EvoCondition {
   public EnumType type;

   public MoveTypeCondition() {
      super("moveType");
      this.type = EnumType.Dragon;
   }

   public MoveTypeCondition(EnumType type) {
      this();
      this.type = type;
   }

   public boolean passes(EntityPixelmon pixelmon) {
      Moveset moveset = pixelmon.getPokemonData().getMoveset();
      Attack[] var3 = moveset.attacks;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Attack a = var3[var5];
         if (a != null && a.getMove().getAttackType() == this.type) {
            return true;
         }
      }

      return false;
   }
}

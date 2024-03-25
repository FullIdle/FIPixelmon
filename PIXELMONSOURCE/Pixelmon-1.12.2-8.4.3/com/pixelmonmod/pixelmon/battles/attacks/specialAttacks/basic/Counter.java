package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class Counter extends DamageReflect {
   public Counter() {
      super(2.0F);
   }

   public boolean isCorrectCategory(AttackCategory category) {
      return category == AttackCategory.PHYSICAL;
   }
}

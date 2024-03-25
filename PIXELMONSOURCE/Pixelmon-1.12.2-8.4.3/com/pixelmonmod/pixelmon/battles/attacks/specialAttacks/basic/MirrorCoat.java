package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class MirrorCoat extends DamageReflect {
   public MirrorCoat() {
      super(2.0F);
   }

   public boolean isCorrectCategory(AttackCategory category) {
      return category == AttackCategory.SPECIAL;
   }
}

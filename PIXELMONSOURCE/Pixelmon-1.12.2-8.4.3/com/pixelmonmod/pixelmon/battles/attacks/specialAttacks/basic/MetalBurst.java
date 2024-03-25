package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class MetalBurst extends DamageReflect {
   public MetalBurst() {
      super(1.5F);
   }

   public boolean isCorrectCategory(AttackCategory category) {
      return category != AttackCategory.STATUS;
   }
}

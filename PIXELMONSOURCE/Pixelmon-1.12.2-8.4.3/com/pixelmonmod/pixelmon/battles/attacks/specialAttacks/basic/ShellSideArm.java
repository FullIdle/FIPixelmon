package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class ShellSideArm extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      int special = user.attack.doDamageCalc(user, target, 1.0);
      int physical = user.attack.doDamageCalc(user, target, 1.0);
      if (special > physical) {
         user.attack.overrideAttackCategory(AttackCategory.SPECIAL);
         user.attack.getMove().setMakesContact(false);
      } else if (special == physical && Math.random() > 0.5) {
         user.attack.overrideAttackCategory(AttackCategory.SPECIAL);
         user.attack.getMove().setMakesContact(false);
      }

      return AttackResult.proceed;
   }
}

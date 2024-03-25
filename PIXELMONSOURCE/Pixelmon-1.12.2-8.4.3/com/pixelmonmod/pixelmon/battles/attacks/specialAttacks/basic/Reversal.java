package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Reversal extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      int percentage = (int)user.getHealthPercent();
      if (percentage >= 71) {
         user.attack.getMove().setBasePower(20);
      } else if (percentage >= 36) {
         user.attack.getMove().setBasePower(40);
      } else if (percentage >= 21) {
         user.attack.getMove().setBasePower(80);
      } else if (percentage >= 11) {
         user.attack.getMove().setBasePower(100);
      } else if (percentage >= 5) {
         user.attack.getMove().setBasePower(150);
      } else {
         user.attack.getMove().setBasePower(200);
      }

      return AttackResult.proceed;
   }
}

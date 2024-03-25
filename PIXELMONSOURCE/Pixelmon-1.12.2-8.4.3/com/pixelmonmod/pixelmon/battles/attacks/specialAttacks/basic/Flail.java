package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Flail extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      double healthPercent = (double)user.getHealthPercent();
      if (healthPercent < 3.0) {
         user.attack.getMove().setBasePower(200);
      } else if (healthPercent < 10.0) {
         user.attack.getMove().setBasePower(150);
      } else if (healthPercent < 20.0) {
         user.attack.getMove().setBasePower(100);
      } else if (healthPercent < 33.0) {
         user.attack.getMove().setBasePower(80);
      } else if (healthPercent < 66.0) {
         user.attack.getMove().setBasePower(40);
      } else {
         user.attack.getMove().setBasePower(20);
      }

      return AttackResult.proceed;
   }
}

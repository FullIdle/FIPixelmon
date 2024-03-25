package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class FusionBolt extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.turn > 0) {
         PixelmonWrapper turnBefore = (PixelmonWrapper)user.bc.turnList.get(user.bc.turn - 1);
         if (turnBefore.attack != null && turnBefore.attack.isAttack("Blue Flare", "Fusion Flare")) {
            user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() * 2);
         }
      }

      return AttackResult.proceed;
   }
}

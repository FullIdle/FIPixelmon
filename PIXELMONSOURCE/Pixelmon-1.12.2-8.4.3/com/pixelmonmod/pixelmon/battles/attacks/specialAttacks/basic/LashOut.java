package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class LashOut extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.getBattleStats().isLoweredThisTurn()) {
         user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() * 2);
         target.bc.sendToAll("pixelmon.status.lashout", user.getNickname());
      }

      return AttackResult.proceed;
   }
}

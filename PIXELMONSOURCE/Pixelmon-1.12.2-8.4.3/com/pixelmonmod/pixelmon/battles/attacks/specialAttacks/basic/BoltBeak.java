package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class BoltBeak extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      for(int i = user.bc.turn + 1; i < user.bc.turnList.size(); ++i) {
         if (user.bc.turnList.get(i) == target) {
            user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
            return AttackResult.proceed;
         }
      }

      if (target.switchedThisTurn) {
         user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
      }

      return AttackResult.proceed;
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class FocusEnergy extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.attack.isAttack("Focus Energy")) {
         user.bc.sendToAll("pixelmon.effect.critincreased", user.getNickname());
      } else if (user.attack.isZ) {
         user.bc.sendToAll("pixelmon.effect.zcritincreased", user.getNickname());
      }

      if (!user.getBattleStats().increaseCritStage(2, user.attack == null || !user.attack.isZ)) {
         user.bc.sendToAll("pixelmon.battletext.movefailed");
         return AttackResult.failed;
      } else {
         return AttackResult.proceed;
      }
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class PollenPuff extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.isAlly(target) && !target.isFainted()) {
         target.healByPercent(50.0F);
         return AttackResult.ignore;
      } else {
         return super.applyEffectDuring(user, target);
      }
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class TeaTime extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasHeldItem() && target.getHeldItem().isBerry()) {
         target.consumeItem();
      }

      return super.applyEffectDuring(user, target);
   }
}

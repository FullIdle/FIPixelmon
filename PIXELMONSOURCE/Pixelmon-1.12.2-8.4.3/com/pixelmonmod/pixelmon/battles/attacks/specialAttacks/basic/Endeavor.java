package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Endeavor extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      float userHealth = (float)user.getHealth(true);
      float targetHealth = (float)target.getHealth(true);
      if (userHealth >= targetHealth) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         int damage = (int)(targetHealth - userHealth);
         target.doBattleDamage(user, (float)damage, DamageTypeEnum.ATTACKFIXED);
         return AttackResult.hit;
      }
   }
}

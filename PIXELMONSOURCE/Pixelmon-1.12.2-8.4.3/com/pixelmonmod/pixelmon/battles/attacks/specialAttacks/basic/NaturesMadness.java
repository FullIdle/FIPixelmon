package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class NaturesMadness extends SpecialAttackBase {
   float percent = 0.5F;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      double damage = (double)((float)target.getHealth() * this.percent);
      target.doBattleDamage(user, (float)damage, DamageTypeEnum.ATTACKFIXED);
      return AttackResult.hit;
   }
}

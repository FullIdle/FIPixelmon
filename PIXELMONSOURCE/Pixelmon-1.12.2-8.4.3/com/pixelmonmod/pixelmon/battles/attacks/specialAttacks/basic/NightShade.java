package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ParentalBond;

public class NightShade extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      int damage = user.getLevelNum();
      target.doBattleDamage(user, (float)damage, DamageTypeEnum.ATTACKFIXED);
      boolean hasParentalBond = user.getBattleAbility() instanceof ParentalBond;
      if (hasParentalBond && user.isAlive() && target.isAlive()) {
         target.doBattleDamage(user, (float)damage, DamageTypeEnum.ATTACKFIXED);
         user.bc.sendToAll("multiplehit.times", user.getNickname(), 2);
      }

      return AttackResult.hit;
   }
}

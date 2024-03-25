package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;

public class JumpKick extends SpecialAttackBase {
   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!(user.getBattleAbility() instanceof MagicGuard) && !user.bc.globalStatusController.hasStatus(StatusType.Gravity)) {
         user.bc.sendToAll("pixelmon.effect.hurtlanding", user.getNickname());
         user.doBattleDamage(user, (float)user.getPercentMaxHealth(50.0F), DamageTypeEnum.CRASH);
      }
   }
}

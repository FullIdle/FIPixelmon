package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class Venoshock extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      user.attack.getMove().setBasePower(target.hasStatus(StatusType.Poison, StatusType.PoisonBadly) ? 130 : 65);
      return AttackResult.proceed;
   }
}

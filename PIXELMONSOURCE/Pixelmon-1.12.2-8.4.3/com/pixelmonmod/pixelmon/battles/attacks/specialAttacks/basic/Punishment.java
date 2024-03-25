package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Punishment extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      int DamageMultiplier = target.getBattleStats().getSumIncreases();
      user.attack.getMove().setBasePower(60 + 20 * DamageMultiplier);
      if (user.attack.getMove().getBasePower() >= 200) {
         user.attack.getMove().setBasePower(200);
      }

      return AttackResult.proceed;
   }
}

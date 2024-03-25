package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Pressure;

public class TrumpCard extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      int afterPP = user.attack.pp - 1;
      if (target.getBattleAbility() instanceof Pressure) {
         --afterPP;
      }

      if (afterPP >= 4) {
         user.attack.getMove().setBasePower(40);
      } else if (afterPP == 3) {
         user.attack.getMove().setBasePower(50);
      } else if (afterPP == 2) {
         user.attack.getMove().setBasePower(60);
      } else if (afterPP == 1) {
         user.attack.getMove().setBasePower(80);
      } else {
         user.attack.getMove().setBasePower(200);
      }

      return AttackResult.proceed;
   }
}

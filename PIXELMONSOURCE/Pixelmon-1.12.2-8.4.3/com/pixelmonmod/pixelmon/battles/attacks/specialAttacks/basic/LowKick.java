package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;

public class LowKick extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.isDynamax()) {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         float weight = target.getWeight(AbilityBase.ignoreAbility(user, target));
         if ((double)weight >= 200.0) {
            user.attack.getMove().setBasePower(120);
         } else if ((double)weight >= 100.0) {
            user.attack.getMove().setBasePower(100);
         } else if ((double)weight >= 50.0) {
            user.attack.getMove().setBasePower(80);
         } else if ((double)weight >= 25.0) {
            user.attack.getMove().setBasePower(60);
         } else if ((double)weight >= 10.0) {
            user.attack.getMove().setBasePower(40);
         } else {
            user.attack.getMove().setBasePower(20);
         }

         return AttackResult.proceed;
      }
   }
}

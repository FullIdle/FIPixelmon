package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;

public class HeavySlam extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.isDynamax()) {
         user.bc.sendToAll("pixelmon.battletext.wontuse", user.getNickname());
         return AttackResult.failed;
      } else {
         float i = target.getWeight(AbilityBase.ignoreAbility(user, target)) / user.getWeight(false) * 100.0F;
         if (i <= 20.0F) {
            user.attack.getMove().setBasePower(120);
         } else if (i > 20.0F && i <= 25.0F) {
            user.attack.getMove().setBasePower(100);
         } else if (i > 25.0F && (double)i <= 33.333333333333336) {
            user.attack.getMove().setBasePower(80);
         } else if ((double)i > 33.333333333333336) {
            user.attack.getMove().setBasePower(60);
         } else {
            user.attack.getMove().setBasePower(40);
         }

         return AttackResult.proceed;
      }
   }
}

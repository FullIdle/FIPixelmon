package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Present extends SpecialAttackBase {
   transient int r;

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      this.r = RandomHelper.getRandomNumberBetween(1, 100);
      if (user.bc.simulateMode) {
         this.r = 0;
         user.attack.getMove().setBasePower(52);
      } else if (this.r <= 40) {
         user.attack.getMove().setBasePower(40);
      } else if (this.r <= 70) {
         user.attack.getMove().setBasePower(80);
      } else if (this.r <= 80) {
         user.attack.getMove().setBasePower(120);
      }

      return AttackResult.proceed;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.r > 80) {
         double restoration = (double)target.getMaxHealth() * 0.25;
         if (restoration > (double)(target.getMaxHealth() - target.getHealth())) {
            restoration = (double)(target.getMaxHealth() - target.getHealth());
         }

         if (restoration <= 0.0) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }

         target.healEntityBy((int)restoration);
         user.bc.sendToAll("pixelmon.effect.healother", user.getNickname(), target.getNickname());
      }

      return AttackResult.proceed;
   }
}

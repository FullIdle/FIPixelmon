package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Magnitude extends SpecialAttackBase {
   public transient int magnitude;

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0) {
         int i = RandomHelper.getRandomNumberBetween(1, 100);
         this.magnitude = 0;
         if (i <= 5) {
            this.magnitude = 4;
         } else if (i <= 15 && i > 5) {
            this.magnitude = 5;
         } else if (i <= 35 && i > 15) {
            this.magnitude = 6;
         } else if (i <= 65 && i > 35) {
            this.magnitude = 7;
         } else if (i <= 85 && i > 65) {
            this.magnitude = 8;
         } else if (i <= 95 && i > 85) {
            this.magnitude = 9;
         } else if (i <= 100 && i > 95) {
            this.magnitude = 10;
         }

         user.bc.sendToAll("pixelmon.effect.magnitude", this.magnitude);
      }

      if (user.bc.simulateMode) {
         user.attack.getMove().setBasePower(71);
      } else if (this.magnitude == 4) {
         user.attack.getMove().setBasePower(10);
      } else if (this.magnitude == 5) {
         user.attack.getMove().setBasePower(30);
      } else if (this.magnitude == 6) {
         user.attack.getMove().setBasePower(50);
      } else if (this.magnitude == 7) {
         user.attack.getMove().setBasePower(70);
      } else if (this.magnitude == 8) {
         user.attack.getMove().setBasePower(90);
      } else if (this.magnitude == 9) {
         user.attack.getMove().setBasePower(110);
      } else if (this.magnitude == 10) {
         user.attack.getMove().setBasePower(150);
      } else {
         user.attack.getMove().setBasePower(10);
      }

      return AttackResult.proceed;
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import java.util.ArrayList;

public class SolarBeam extends MultiTurnCharge {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!this.doesPersist(user)) {
         this.setPersists(user, true);
         this.setTurnCount(user, 2);
      }

      this.decrementTurnCount(user);
      if (this.getTurnCount(user) == 1 && !(user.bc.globalStatusController.getWeather() instanceof Sunny) && !user.getUsableHeldItem().affectMultiturnMove(user)) {
         user.bc.sendToAll("pixelmon.effect.storeenergy", user.getNickname());
         return AttackResult.charging;
      } else {
         this.setPersists(user, false);
         return AttackResult.proceed;
      }
   }

   public boolean shouldNotLosePP(PixelmonWrapper user) {
      return !(user.bc.globalStatusController.getWeather() instanceof Sunny) && super.shouldNotLosePP(user);
   }

   public boolean isCharging(PixelmonWrapper user, PixelmonWrapper target) {
      return super.isCharging(user, target) && !(user.bc.globalStatusController.getWeather() instanceof Sunny);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!(pw.bc.globalStatusController.getWeather() instanceof Sunny)) {
         super.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      }

   }
}

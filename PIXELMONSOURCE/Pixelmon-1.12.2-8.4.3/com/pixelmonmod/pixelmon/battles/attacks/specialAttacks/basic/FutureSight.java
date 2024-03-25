package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.FutureSighted;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import java.util.ArrayList;

public class FutureSight extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      FutureSighted status = (FutureSighted)target.getStatus(StatusType.FutureSight);
      if (status == null) {
         if (user.bc.simulateMode) {
            return AttackResult.proceed;
         } else {
            user.bc.sendToAll("pixelmon.effect.foresawattack", user.getNickname());
            target.addStatus(new FutureSighted(user, user.attack), target);
            return AttackResult.succeeded;
         }
      } else if (status.getRemainingTurns() <= 0) {
         return AttackResult.proceed;
      } else {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.lowerTier(2);
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.UproarStatus;
import java.util.ArrayList;

public class Uproar extends MultiTurnSpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!this.doesPersist(user)) {
         this.setPersists(user, true);
         this.setTurnCount(user, 2);
         user.addStatus(new UproarStatus(), user);
         user.bc.sendToAll("pixelmon.effect.uproar", user.getNickname());
      }

      if (this.getTurnCount(user) == 0) {
         this.setPersists(user, false);
         user.removeStatus(StatusType.Uproar);
         user.bc.sendToAll("pixelmon.effect.uproarend", user.getNickname());
      }

      this.decrementTurnCount(user);
      return AttackResult.proceed;
   }

   public void removeEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.setPersists(user, false);
      user.removeStatus(StatusType.Uproar);
   }

   public boolean shouldNotLosePP(PixelmonWrapper user) {
      return this.doesPersist(user);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (userChoice.tier >= 3) {
         userChoice.weight /= 2.0F;
      }

   }
}

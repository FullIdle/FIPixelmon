package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import java.util.ArrayList;

public class Rollout extends MultiTurnSpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (!this.doesPersist(user)) {
         this.setPersists(user, true);
         this.setTurnCount(user, 5);
      }

      int turnCount = this.getTurnCount(user);
      if (turnCount == 0) {
         turnCount = 5;
      }

      user.attack.getMove().setBasePower(30 << 5 - turnCount);
      if (user.hasStatus(StatusType.DefenseCurl)) {
         user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() * 2);
      }

      this.decrementTurnCount(user);
      if (this.getTurnCount(user) <= 0) {
         this.setPersists(user, false);
      }

      return AttackResult.proceed;
   }

   public void removeEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.setPersists(user, false);
   }

   public boolean shouldNotLosePP(PixelmonWrapper user) {
      return this.getTurnCount(user) < 4;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (userChoice.tier >= 3) {
         userChoice.weight /= 2.0F;
      }

   }
}

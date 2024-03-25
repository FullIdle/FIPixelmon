package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class SkullBash extends MultiTurnCharge {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!this.doesPersist(user)) {
         this.setPersists(user, true);
         this.setTurnCount(user, 2);
      }

      this.decrementTurnCount(user);
      if (this.getTurnCount(user) == 1) {
         user.bc.sendToAll("pixelmon.effect.skullbash", user.getNickname());
         user.getBattleStats().modifyStat(1, (StatsType)StatsType.Defence);
         if (!user.getUsableHeldItem().affectMultiturnMove(user)) {
            return AttackResult.charging;
         }
      }

      this.setPersists(user, false);
      return AttackResult.proceed;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      super.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      StatsEffect statsEffect = new StatsEffect(StatsType.Defence, 1, true);
      statsEffect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}

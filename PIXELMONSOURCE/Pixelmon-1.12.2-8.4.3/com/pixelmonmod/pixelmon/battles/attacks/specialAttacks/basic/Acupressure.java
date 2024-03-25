package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class Acupressure extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.getBattleStats().raiseRandomStat(2)) {
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int numPossibilities = 0;
      Iterator var8 = userChoice.targets.iterator();

      while(var8.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var8.next();
         ArrayList possibleStats = target.getBattleStats().getPossibleStatIncreases();

         for(Iterator var11 = possibleStats.iterator(); var11.hasNext(); ++numPossibilities) {
            Integer stat = (Integer)var11.next();
            StatsEffect statBoost = new StatsEffect(target.getBattleStats().getStageEnum(stat), 2, target == pw);
            statBoost.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
         }
      }

      if (numPossibilities > 0) {
         userChoice.weight /= (float)numPossibilities;
      }

   }
}

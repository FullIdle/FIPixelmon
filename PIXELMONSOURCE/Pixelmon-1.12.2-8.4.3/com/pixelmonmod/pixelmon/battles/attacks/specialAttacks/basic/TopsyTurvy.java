package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BattleStats;
import java.util.ArrayList;
import java.util.Iterator;

public class TopsyTurvy extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.getBattleStats().isStatModified()) {
         user.bc.sendToAll("pixelmon.effect.topsyturvy", target.getNickname());
         target.getBattleStats().reverseStats();
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
      }

      return AttackResult.succeeded;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(var7.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            if (target.getBattleStats().isStatModified()) {
               pw.bc.simulateMode = false;
               BattleStats saveStats = new BattleStats(target.getBattleStats());
               ArrayList opponents = pw.getOpponentPokemon();
               BattleAIBase ai = pw.getBattleAI();

               try {
                  target.getBattleStats().reverseStats();
                  pw.bc.simulateMode = true;
                  ArrayList bestUserChoicesAfter = ai.getBestAttackChoices(pw);
                  ArrayList bestOpponentChoicesAfter = ai.getBestAttackChoices(opponents);
                  ai.weightFromUserOptions(pw, userChoice, bestUserChoices, bestUserChoicesAfter);
                  ai.weightFromOpponentOptions(pw, userChoice, MoveChoice.splitChoices(opponents, bestOpponentChoices), bestOpponentChoicesAfter);
               } finally {
                  pw.bc.simulateMode = false;
                  target.getBattleStats().copyStats(saveStats);
                  pw.bc.simulateMode = true;
               }
            }
         }

      }
   }
}

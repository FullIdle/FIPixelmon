package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BattleStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Swap extends SpecialAttackBase {
   String langString;
   StatsType[] swapStats;

   public Swap() {
   }

   public Swap(String langString, StatsType... swapStats) {
      this.langString = langString;
      this.swapStats = swapStats;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll(this.langString, user.getNickname(), target.getNickname());
      user.getBattleStats().swapStats(target.getBattleStats(), this.swapStats);
      return AttackResult.succeeded;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         for(Iterator var7 = userChoice.targets.iterator(); var7.hasNext(); pw.bc.simulateMode = true) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            BattleStats[] originalStats = new BattleStats[]{pw.getBattleStats(), target.getBattleStats()};
            StatsType[] var10 = this.swapStats;
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               StatsType stat = var10[var12];
               if (originalStats[0].getStage(stat) < originalStats[1].getStage(stat)) {
                  break;
               }
            }

            BattleStats[] saveStats = new BattleStats[2];
            pw.bc.simulateMode = false;
            saveStats[0] = new BattleStats(originalStats[0]);
            saveStats[1] = new BattleStats(originalStats[1]);
            ArrayList opponents = pw.getOpponentPokemon();
            BattleAIBase ai = pw.getBattleAI();
            boolean var18 = false;

            try {
               var18 = true;
               pw.getBattleStats().swapStats(target.getBattleStats(), this.swapStats);
               pw.bc.simulateMode = true;
               ArrayList bestUserChoicesAfter = ai.getBestAttackChoices(pw);
               ArrayList bestOpponentChoicesAfter = ai.getBestAttackChoices(opponents);
               ai.weightFromUserOptions(pw, userChoice, bestUserChoices, bestUserChoicesAfter);
               ai.weightFromOpponentOptions(pw, userChoice, MoveChoice.splitChoices(opponents, bestOpponentChoices), bestOpponentChoicesAfter);
               var18 = false;
            } finally {
               if (var18) {
                  pw.bc.simulateMode = false;

                  for(int i = 0; i < saveStats.length; ++i) {
                     originalStats[i].copyStats(saveStats[i]);
                  }

                  pw.bc.simulateMode = true;
               }
            }

            pw.bc.simulateMode = false;

            for(int i = 0; i < saveStats.length; ++i) {
               originalStats[i].copyStats(saveStats[i]);
            }
         }

      }
   }
}

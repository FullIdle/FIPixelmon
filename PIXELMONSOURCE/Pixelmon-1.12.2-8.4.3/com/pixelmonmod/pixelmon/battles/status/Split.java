package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Split extends StatusBase {
   StatsType stat1;
   StatsType stat2;
   int statValue1;
   int statValue2;
   String langString;

   public Split(StatusType type, StatsType stat1, StatsType stat2, String langString) {
      super(type);
      this.stat1 = stat1;
      this.stat2 = stat2;
      this.langString = langString;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      int[] userStats = user.getBattleStats().getBaseBattleStats();
      int[] targetStats = target.getBattleStats().getBaseBattleStats();
      int splitStat1 = (userStats[this.stat1.getStatIndex()] + targetStats[this.stat1.getStatIndex()]) / 2;
      int splitStat2 = (userStats[this.stat2.getStatIndex()] + targetStats[this.stat2.getStatIndex()]) / 2;
      user.removeStatus(this.type);
      target.removeStatus(this.type);
      if (target.addStatus(this.getNewInstance(splitStat1, splitStat2), user) && user.addStatus(this.getNewInstance(splitStat1, splitStat2), user)) {
         user.bc.sendToAll(this.langString, user.getNickname(), target.getNickname());
      }

   }

   protected abstract Split getNewInstance(int var1, int var2);

   public int[] modifyBaseStats(PixelmonWrapper user, int[] stats) {
      stats[this.stat1.getStatIndex()] = this.statValue1;
      stats[this.stat2.getStatIndex()] = this.statValue2;
      return stats;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         StatusBase userSplit = pw.getStatus(this.type);
         ArrayList opponents = pw.getOpponentPokemon();
         Iterator var9 = userChoice.targets.iterator();

         while(var9.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var9.next();
            StatusBase targetSplit = target.getStatus(this.type);
            BattleAIBase ai = pw.getBattleAI();

            try {
               pw.bc.simulateMode = false;
               pw.bc.sendMessages = false;
               this.applyEffect(pw, target);
               pw.bc.simulateMode = true;
               pw.bc.modifyStats();
               pw.bc.modifyStatsCancellable(pw);
               ArrayList bestUserChoicesAfter = ai.getBestAttackChoices(pw);
               ArrayList bestOpponentChoicesAfter = ai.getBestAttackChoices(opponents);
               ai.weightFromUserOptions(pw, userChoice, bestUserChoices, bestUserChoicesAfter);
               ai.weightFromOpponentOptions(pw, userChoice, MoveChoice.splitChoices(opponents, bestOpponentChoices), bestOpponentChoicesAfter);
            } finally {
               pw.bc.simulateMode = false;
               this.restoreSplit(pw, userSplit);
               this.restoreSplit(target, targetSplit);
               pw.bc.simulateMode = true;
               pw.bc.sendMessages = true;
               pw.bc.modifyStats();
               pw.bc.modifyStatsCancellable(pw);
            }
         }

      }
   }

   private void restoreSplit(PixelmonWrapper pw, StatusBase split) {
      pw.removeStatus(this.type);
      if (split != null) {
         pw.addStatus(split, pw);
      }

   }
}

package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class PowerTrick extends StatusBase {
   public PowerTrick() {
      super(StatusType.PowerTrick);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.status.powertrick", user.getNickname());
      if (!user.removeStatus(this.type)) {
         user.addStatus(new PowerTrick(), user);
      }

   }

   public int[] modifyBaseStats(PixelmonWrapper user, int[] stats) {
      int temp = stats[StatsType.Attack.getStatIndex()];
      stats[StatsType.Attack.getStatIndex()] = stats[StatsType.Defence.getStatIndex()];
      stats[StatsType.Defence.getStatIndex()] = temp;
      return stats;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      StatusBase prevStatus = pw.getStatus(this.type);
      ArrayList opponents = pw.getOpponentPokemon();
      BattleAIBase ai = pw.getBattleAI();

      try {
         pw.bc.simulateMode = false;
         pw.bc.sendMessages = false;
         this.applyEffect(pw, pw);
         pw.bc.simulateMode = true;
         pw.bc.modifyStats();
         pw.bc.modifyStatsCancellable(pw);
         ArrayList bestUserChoicesAfter = ai.getBestAttackChoices(pw);
         ArrayList bestOpponentChoicesAfter = ai.getBestAttackChoices(opponents);
         ai.weightFromUserOptions(pw, userChoice, bestUserChoices, bestUserChoicesAfter);
         ai.weightFromOpponentOptions(pw, userChoice, MoveChoice.splitChoices(opponents, bestOpponentChoices), bestOpponentChoicesAfter);
      } finally {
         pw.bc.simulateMode = false;
         pw.removeStatus(this.type);
         if (prevStatus != null) {
            pw.addStatus(prevStatus, pw);
         }

         pw.bc.simulateMode = true;
         pw.bc.sendMessages = true;
         pw.bc.modifyStats();
         pw.bc.modifyStatsCancellable(pw);
      }

   }
}

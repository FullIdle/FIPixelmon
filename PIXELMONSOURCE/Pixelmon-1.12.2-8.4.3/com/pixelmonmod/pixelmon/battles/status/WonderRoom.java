package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class WonderRoom extends GlobalStatusBase {
   private transient int duration = 5;

   public WonderRoom() {
      super(StatusType.WonderRoom);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.removeGlobalStatus(this.type)) {
         user.bc.sendToAll("pixelmon.status.wonderroomend");
      } else {
         user.bc.sendToAll("pixelmon.status.wonderroom");
         user.bc.globalStatusController.addGlobalStatus(new WonderRoom());
      }

   }

   public int[] modifyBaseStats(PixelmonWrapper user, int[] stats) {
      int tempDefense = stats[StatsType.Defence.getStatIndex()];
      stats[StatsType.Defence.getStatIndex()] = stats[StatsType.SpecialDefence.getStatIndex()];
      stats[StatsType.SpecialDefence.getStatIndex()] = tempDefense;
      return stats;
   }

   public void applyRepeatedEffect(GlobalStatusController globalStatusController) {
      if (--this.duration <= 0) {
         globalStatusController.bc.sendToAll("pixelmon.status.wonderroomend");
         globalStatusController.removeGlobalStatus((GlobalStatusBase)this);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      ArrayList opponents = pw.getOpponentPokemon();
      WonderRoom wonderRoom = (WonderRoom)pw.bc.globalStatusController.getGlobalStatus(this.type);
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
         if (wonderRoom != null) {
            pw.bc.globalStatusController.addGlobalStatus(wonderRoom);
         }

         pw.bc.simulateMode = true;
         pw.bc.sendMessages = true;
         pw.bc.modifyStats();
         pw.bc.modifyStatsCancellable(pw);
      }

   }
}

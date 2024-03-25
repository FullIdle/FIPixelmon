package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;

public class TrickRoom extends GlobalStatusBase {
   private transient int effectTurns = 5;

   public TrickRoom() {
      super(StatusType.TrickRoom);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.removeGlobalStatus(this.type)) {
         user.bc.sendToAll("pixelmon.effect.trickroomnormal");
      } else {
         user.bc.sendToAll("pixelmon.status.trickroom", user.getNickname());
         user.addGlobalStatus(new TrickRoom());
      }

   }

   public void applyRepeatedEffect(GlobalStatusController globalStatusController) {
      if (--this.effectTurns <= 0) {
         globalStatusController.bc.sendToAll("pixelmon.status.trickroomreturntonormal");
         globalStatusController.removeGlobalStatus((GlobalStatusBase)this);
      }

   }

   public boolean participantMovesFirst(PixelmonWrapper user, PixelmonWrapper target) throws Exception {
      int userSpeed = user.getBattleStats().getStatWithMod(StatsType.Speed);
      int targetSpeed = target.getBattleStats().getStatWithMod(StatsType.Speed);
      if (userSpeed > targetSpeed) {
         return false;
      } else {
         return targetSpeed > userSpeed ? true : RandomHelper.getRandomChance(50);
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = MoveChoice.splitChoices(pw.getOpponentPokemon(), bestOpponentChoices).iterator();

      ArrayList choices;
      do {
         if (!var7.hasNext()) {
            userChoice.raiseWeight(50.0F);
            return;
         }

         choices = (ArrayList)var7.next();
      } while(MoveChoice.canOutspeed(choices, pw, bestUserChoices));

   }
}

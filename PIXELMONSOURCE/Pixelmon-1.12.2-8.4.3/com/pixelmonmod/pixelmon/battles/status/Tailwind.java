package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class Tailwind extends StatusBase {
   transient int turnsToGo = 4;

   public Tailwind() {
      super(StatusType.Tailwind);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         if (user.hasStatus(this.type)) {
            user.bc.sendToAll("pixelmon.status.tailwindalready");
            user.attack.moveResult.result = AttackResult.failed;
         } else {
            user.addTeamStatus(new Tailwind(), user);
            user.bc.sendToAll("pixelmon.status.tailwindstarted", user.getNickname());
         }
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      int var10001 = StatsType.Speed.getStatIndex();
      stats[var10001] *= 2;
      return stats;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.turnsToGo == 0) {
         pw.removeStatus((StatusBase)this);
         pw.bc.sendToAll("pixelmon.status.tailwindfaded", pw.getNickname());
      }

   }

   public int getRemainingTurns() {
      return this.turnsToGo;
   }

   public StatusBase copy() {
      Tailwind copy = new Tailwind();
      copy.turnsToGo = this.turnsToGo;
      return copy;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!MoveChoice.hasPriority(bestOpponentChoices) && MoveChoice.canOutspeed(bestOpponentChoices, pw, bestUserChoices)) {
         userChoice.raiseWeight(75.0F);
      }

   }
}

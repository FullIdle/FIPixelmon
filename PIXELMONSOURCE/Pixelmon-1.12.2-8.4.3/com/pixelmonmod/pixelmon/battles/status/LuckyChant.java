package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class LuckyChant extends StatusBase {
   transient int duration = 5;

   public LuckyChant() {
      super(StatusType.LuckyChant);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         if (user.addTeamStatus(new LuckyChant(), user)) {
            user.bc.sendToAll("pixelmon.status.luckychant", user.getNickname());
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            user.attack.moveResult.result = AttackResult.failed;
         }
      }

   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.duration <= 0) {
         pw.bc.sendToAll("pixelmon.status.luckychantend", pw.getNickname());
         pw.removeTeamStatus((StatusBase)this);
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public StatusBase copy() {
      LuckyChant copy = new LuckyChant();
      copy.duration = this.duration;
      return copy;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(10.0F);
   }
}

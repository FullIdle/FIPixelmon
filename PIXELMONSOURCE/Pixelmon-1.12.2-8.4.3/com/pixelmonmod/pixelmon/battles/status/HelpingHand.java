package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class HelpingHand extends StatusBase {
   public HelpingHand() {
      super(StatusType.HelpingHand);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.bc.rules.battleType.numPokemon != 1 && !target.hasStatus(StatusType.HelpingHand) && target.bc.getTurnForPokemon(target) >= target.bc.turn) {
         target.bc.sendToAll("pixelmon.status.helpinghand", user.getNickname(), target.getNickname());
         target.addStatus(new HelpingHand(), user);
      } else {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         target.setAttackFailed();
      }

   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{(int)((double)power * 1.5), accuracy};
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(25.0F);
   }
}

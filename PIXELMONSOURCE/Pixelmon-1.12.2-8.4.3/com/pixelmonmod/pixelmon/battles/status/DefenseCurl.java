package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class DefenseCurl extends StatusBase {
   public DefenseCurl() {
      super(StatusType.DefenseCurl);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.addStatus(new DefenseCurl(), user);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!pw.hasStatus(StatusType.DefenseCurl) && MoveChoice.hasSuccessfulAttackChoice(bestUserChoices, "Rollout", "Ice Ball")) {
         userChoice.raiseWeight(50.0F);
      }

   }
}

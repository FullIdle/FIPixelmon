package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers;

import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;

public class CriticalHit extends AttackModifierBase {
   public int stages = 1;

   public CriticalHit(Value... values) {
      if (values.length == 0) {
         this.stages = 0;
      } else {
         this.stages = values[0].value;
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (userChoice.isOffensiveMove()) {
         userChoice.raiseWeight((float)this.stages / 10.0F);
      } else if (pw.getBattleStats().increaseCritStage(1, false)) {
         userChoice.raiseWeight(25.0F);
      }

   }
}

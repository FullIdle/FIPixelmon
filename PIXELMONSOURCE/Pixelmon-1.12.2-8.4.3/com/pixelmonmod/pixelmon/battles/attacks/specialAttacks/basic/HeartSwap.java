package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;
import java.util.Iterator;

public class HeartSwap extends Swap {
   public HeartSwap() {
      super("pixelmon.effect.heartswap", StatsType.Accuracy, StatsType.Evasion, StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         int userStages = pw.getBattleStats().getSumStages();
         Iterator var8 = userChoice.targets.iterator();

         while(var8.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var8.next();
            int targetStages = target.getBattleStats().getSumStages();
            userChoice.raiseWeight((float)((targetStages - userStages) * 20));
         }

      }
   }
}

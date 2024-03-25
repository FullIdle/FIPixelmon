package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class PsychUp extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      user.getBattleStats().copyStats(target.getBattleStats());
      user.bc.sendToAll("pixelmon.effect.psycheup", user.getNickname(), target.getNickname());
      return AttackResult.succeeded;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      int userStages = pw.getBattleStats().getSumStages();
      Iterator var8 = userChoice.targets.iterator();

      while(var8.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var8.next();
         int targetStages = target.getBattleStats().getSumStages();
         userChoice.raiseWeight((float)((targetStages - userStages) * 20));
      }

   }
}

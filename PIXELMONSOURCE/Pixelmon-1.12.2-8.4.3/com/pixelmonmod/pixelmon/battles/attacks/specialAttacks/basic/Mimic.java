package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.util.ArrayList;

public class Mimic extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.simulateMode) {
         return AttackResult.succeeded;
      } else {
         Moveset originalMoveset = user.getMoveset();
         if (target.lastAttack != null && !target.lastAttack.isAttack("Chatter", "Metronome", "Sketch", "Struggle") && !originalMoveset.hasAttack(target.lastAttack)) {
            Moveset moveset = originalMoveset.copy();
            boolean valid = moveset.replaceMove("Mimic", new Attack(target.lastAttack.getMove()));
            if (valid) {
               user.bc.sendToAll("pixelmon.effect.mimic", user.getNickname(), target.getNickname(), target.lastAttack.getMove().getTranslatedName());
               user.setTemporaryMoveset(moveset);
               return AttackResult.succeeded;
            }
         }

         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      MirrorMove mirrorMove = new MirrorMove();
      mirrorMove.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      userChoice.lowerTier(2);
   }
}

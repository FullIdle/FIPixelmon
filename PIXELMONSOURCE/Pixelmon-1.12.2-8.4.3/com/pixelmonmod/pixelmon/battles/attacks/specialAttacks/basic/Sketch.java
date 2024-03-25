package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.util.ArrayList;

public class Sketch extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      Attack copiedAttack = target.lastAttack;
      Moveset moveset = user.getMoveset();
      if (copiedAttack != null && !copiedAttack.isAttack("Chatter", "Struggle") && !moveset.contains(copiedAttack) && !user.hasStatus(StatusType.Transformed) && moveset.hasAttack("Sketch") && !copiedAttack.isZ && !copiedAttack.isMax) {
         if (target.hasStatus(StatusType.CraftyShield)) {
            user.bc.sendToAll("pixelmon.status.craftyshieldprotect", target.getNickname());
            return AttackResult.failed;
         } else {
            if (!user.bc.simulateMode) {
               boolean isTrainer = user.getParticipant().getType() == ParticipantType.Trainer;
               if (isTrainer) {
                  moveset = moveset.copy();
               }

               moveset.replaceMove("Sketch", new Attack(copiedAttack.getMove()));
               if (isTrainer) {
                  user.setTemporaryMoveset(moveset);
               }
            }

            user.bc.sendToAll("pixelmon.effect.sketch", user.getNickname(), copiedAttack.getMove().getTranslatedName());
            return AttackResult.succeeded;
         }
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Mimic mimic = new Mimic();
      mimic.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}

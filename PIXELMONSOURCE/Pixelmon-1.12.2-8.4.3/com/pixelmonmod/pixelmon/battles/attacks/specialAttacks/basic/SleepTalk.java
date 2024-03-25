package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sleep;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Comatose;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SleepTalk extends SpecialAttackBase {
   private static final String[] notAllowedList = new String[]{"Bounce", "Chatter", "Copycat", "Dig", "Dive", "Fly", "Focus Punch", "Me First", "Metronome", "Mirror Move", "Shadow Force", "Sketch", "Skull Bash", "Sky attack", "SolarBeam", "Razor Wind", "Uproar", "Sleep Talk"};

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.hasStatus(StatusType.Sleep) && !(user.getBattleAbility() instanceof Comatose)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         ArrayList possibleAttacks = this.getPossibleAttacks(user);
         if (!possibleAttacks.isEmpty()) {
            Attack a = (Attack)RandomHelper.getRandomElementFromList(possibleAttacks);
            user.useTempAttack(a);
            return AttackResult.ignore;
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      }
   }

   private boolean canSelect(Attack a) {
      return !a.isAttack(notAllowedList);
   }

   private ArrayList getPossibleAttacks(PixelmonWrapper user) {
      return (ArrayList)user.getMoveset().stream().filter(this::canSelect).collect(Collectors.toList());
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Sleep sleep = (Sleep)pw.getStatus(StatusType.Sleep);
      if (sleep != null && sleep.effectTurns != 0) {
         ArrayList possibleAttacks = this.getPossibleAttacks(pw);
         if (!possibleAttacks.isEmpty()) {
            pw.getBattleAI().weightRandomMove(pw, userChoice, MoveChoice.createMoveChoicesFromList(possibleAttacks, pw));
            if (userChoice.weight > 0.0F) {
               userChoice.raiseTier(4);
            }

         }
      }
   }
}

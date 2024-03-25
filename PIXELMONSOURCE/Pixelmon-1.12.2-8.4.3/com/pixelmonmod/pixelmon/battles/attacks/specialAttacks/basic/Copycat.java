package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Set;

public class Copycat extends SpecialAttackBase {
   private static final Set uncopyableMoves = Sets.newHashSet(new String[]{"Baneful Bunker", "Beak Blast", "Behemoth Blast", "Behemoth Blade", "Bestow", "Celebrate", "Chatter", "Circle Throw", "Counter", "Covet", "Destiny Bond", "Detect", "Dragon Tail", "Dynamax Cannon", "Endure", "Feint", "Focus Punch", "Follow Me", "Helping Hand", "Hold Hands", "King's Shield", "Mat Block", "Mirror Coat", "Protect", "Rage Powder", "Roar", "Shell Trap", "Sketch", "Struggle", "Spiky Shield", "Spotlight", "Switcheroo", "Thief", "Transform", "Trick", "Whirlwind"});
   private static final Set copyableTempMoves = Sets.newHashSet(new String[]{"Assist", "Copycat", "Metronome", "Me First", "Mimic", "Mirror Move", "Sleep Talk", "Snatch"});

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.simulateMode) {
         return AttackResult.ignore;
      } else if (user.bc.lastAttack != null && !user.bc.lastAttack.isZ && !uncopyableMoves.contains(user.bc.lastAttack.toString())) {
         if (copyableTempMoves.contains(user.bc.lastAttack.toString())) {
            if (user.bc.lastTempAttack == null) {
               user.bc.sendToAll("pixelmon.effect.effectfailed");
               return AttackResult.failed;
            }

            user.useTempAttack(user.bc.lastTempAttack);
         } else {
            user.useTempAttack(user.bc.lastAttack.isMax ? user.bc.lastAttack.originalMove : user.bc.lastAttack);
         }

         return AttackResult.ignore;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      ArrayList possibleChoices;
      if (MoveChoice.canOutspeed(bestOpponentChoices, pw, userChoice.createList())) {
         possibleChoices = MoveChoice.createChoicesFromChoices(pw, bestOpponentChoices, false);
      } else {
         if (pw.bc.lastAttack == null) {
            return;
         }

         possibleChoices = pw.bc.lastAttack.createMoveChoices(pw, false);
      }

      pw.getBattleAI().weightRandomMove(pw, userChoice, possibleChoices);
   }
}

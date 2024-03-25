package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Assist extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.getParticipant().allPokemon.length == 1) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         ArrayList possibleAttacks = this.getPossibleAttacks(user);
         if (!possibleAttacks.isEmpty()) {
            user.useTempAttack((Attack)RandomHelper.getRandomElementFromList(possibleAttacks));
            return AttackResult.ignore;
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      ArrayList possibleAttacks = this.getPossibleAttacks(pw);
      if (!possibleAttacks.isEmpty()) {
         ArrayList possibleChoices = MoveChoice.createMoveChoicesFromList(possibleAttacks, pw);
         pw.getBattleAI().weightRandomMove(pw, userChoice, possibleChoices);
      }
   }

   private ArrayList getPossibleAttacks(PixelmonWrapper user) {
      ArrayList possibleAttacks = new ArrayList();
      PixelmonWrapper[] var3 = user.getParticipant().allPokemon;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PixelmonWrapper other = var3[var5];
         if (other != user) {
            possibleAttacks.addAll((Collection)other.getMoveset().stream().filter((attack) -> {
               return attack != null && !attack.isAttack("Assist", "Baneful Bunker", "Belch", "Bestow", "Bounce", "Celebrate", "Chatter", "Circle Throw", "Copycat", "Counter", "Covet", "Destiny Bond", "Detect", "Dig", "Dive", "Dragon Tail", "Endure", "Feint", "Fly", "Focus Punch", "Follow Me", "Helping Hand", "Hold Hands", "King's Shield", "Mat Block", "Me First", "Metronome", "Mimic", "Mirror Coat", "Mirror Move", "Nature Power", "Phantom Force", "Protect", "Rage Powder", "Roar", "Shadow Force", "Shell Trap", "Sketch", "Sky Drop", "Sleep Talk", "Snatch", "Spiky Shield", "Spotlight", "Struggle", "Switcheroo", "Thief", "Transform", "Trick", "Whirlwind");
            }).collect(Collectors.toList()));
         }
      }

      return possibleAttacks;
   }
}

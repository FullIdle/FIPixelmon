package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.ArrayList;

public class Minimize extends StatusBase {
   private static final transient String[] squashMoves = new String[]{"Body Slam", "Dragon Rush", "Flying Press", "Heat Crash", "Steamroller", "Stomp", "Heavy Slam", "Malicious Moonsault"};

   public Minimize() {
      super(StatusType.Minimize);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.getBattleStats().modifyStat(2, (StatsType)StatsType.Evasion) && !user.hasStatus(StatusType.Minimize)) {
         user.addStatus(new Minimize(), user);
      }

      StatsEffect.addStatChangeAnimation(user, target, StatsType.Evasion, 2);
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.isAttack(squashMoves)) {
         power *= 2;
         accuracy = -1;
      }

      return new int[]{power, accuracy};
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!MoveChoice.hasMove(opponentChoices, squashMoves)) {
         StatsEffect statsEffect = new StatsEffect(StatsType.Evasion, 2, true);
         statsEffect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      }
   }
}

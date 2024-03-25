package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class Hustle extends AbilityBase {
   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      int var10001 = StatsType.Attack.getStatIndex();
      stats[var10001] = (int)((double)stats[var10001] * 1.5);
      return stats;
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (accuracy > 0) {
         if (a.getAttackCategory() == AttackCategory.PHYSICAL) {
            accuracy -= 20;
         }

         if (accuracy < 0) {
            accuracy = 0;
         }
      }

      return new int[]{power, accuracy};
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class ShieldDust extends AbilityBase {
   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      a.getMove().effects.stream().filter((effect) -> {
         return effect.isChance() && !ignoreAbility(user, target);
      }).forEach((effect) -> {
         effect.changeChance(0);
      });
      return new int[]{power, accuracy};
   }
}

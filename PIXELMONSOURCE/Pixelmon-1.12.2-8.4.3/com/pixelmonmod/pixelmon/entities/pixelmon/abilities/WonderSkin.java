package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class WonderSkin extends AbilityBase {
   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getAttackCategory() == AttackCategory.STATUS && accuracy > 50 && !AbilityBase.ignoreAbility(user, target)) {
         accuracy = 50;
      }

      return new int[]{power, accuracy};
   }
}

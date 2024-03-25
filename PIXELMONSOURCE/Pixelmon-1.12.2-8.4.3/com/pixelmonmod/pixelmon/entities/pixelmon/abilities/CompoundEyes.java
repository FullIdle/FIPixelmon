package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class CompoundEyes extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      accuracy = (int)((double)accuracy * 1.3);
      if (accuracy > 100) {
         accuracy = 100;
      }

      return new int[]{power, accuracy};
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;

public class Rivalry extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user.getGender() == target.getGender()) {
         power = (int)((double)power * 1.25);
         return new int[]{power, accuracy};
      } else {
         if (user.getGender() != Gender.None && target.getGender() != Gender.None) {
            power = (int)((double)power * 0.75);
         }

         return new int[]{power, accuracy};
      }
   }
}

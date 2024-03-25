package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class NoGuard extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, -2};
   }

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, -2};
   }
}

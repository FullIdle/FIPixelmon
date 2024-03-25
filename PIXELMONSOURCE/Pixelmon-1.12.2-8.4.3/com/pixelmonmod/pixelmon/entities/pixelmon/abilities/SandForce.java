package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sandstorm;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class SandForce extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user.bc != null && user.bc.globalStatusController.getWeather() instanceof Sandstorm && (a.getType().equals(EnumType.Rock) || a.getType().equals(EnumType.Ground) || a.getType().equals(EnumType.Steel))) {
         power = (int)((double)power * 1.3);
      }

      return new int[]{power, accuracy};
   }
}

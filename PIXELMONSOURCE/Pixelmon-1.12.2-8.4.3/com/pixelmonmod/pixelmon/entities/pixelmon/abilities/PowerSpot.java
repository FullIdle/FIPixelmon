package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class PowerSpot extends AbilityBase {
   public int[] modifyPowerAndAccuracyTeammate(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{(int)((double)power * 1.3), accuracy};
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class VictoryStar extends AbilityBase {
   public int[] modifyPowerAndAccuracyTeammate(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return new int[]{power, (int)((double)accuracy * 1.1)};
   }
}

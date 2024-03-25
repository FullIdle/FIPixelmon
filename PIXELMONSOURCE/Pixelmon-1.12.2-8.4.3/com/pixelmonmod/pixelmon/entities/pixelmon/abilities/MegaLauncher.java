package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class MegaLauncher extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.isAttack("Aura Sphere", "Dark Pulse", "Dragon Pulse", "Origin Pulse", "Water Pulse", "Terrain Pulse")) {
         power = (int)((double)power * 1.5);
      }

      return new int[]{power, accuracy};
   }
}

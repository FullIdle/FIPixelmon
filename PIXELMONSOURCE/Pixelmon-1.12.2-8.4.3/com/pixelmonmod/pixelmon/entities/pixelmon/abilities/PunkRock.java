package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class PunkRock extends AbilityBase {
   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.isSoundBased()) {
         damage /= 2;
      }

      return damage;
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      return a.isSoundBased() ? new int[]{(int)((double)power * 1.3), accuracy} : new int[]{power, accuracy};
   }
}

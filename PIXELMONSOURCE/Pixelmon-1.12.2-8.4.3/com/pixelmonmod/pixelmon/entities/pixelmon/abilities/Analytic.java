package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Analytic extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (target.bc.battleLog.getActionForPokemon(target.bc.battleTurn, target) != null || target.attack == null) {
         power = (int)((double)power * 1.3);
      }

      return new int[]{power, accuracy};
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class LowHPTypeBoost extends AbilityBase {
   private EnumType type;

   protected LowHPTypeBoost(EnumType type) {
      this.type = type;
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user.getHealthPercent() <= 33.333332F && a.getType() == this.type) {
         power = (int)((double)power * 1.5);
      }

      return new int[]{power, accuracy};
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public class SereneGrace extends AbilityBase {
   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (!user.hasStatus(StatusType.WaterPledge)) {
         a.getMove().effects.stream().filter(EffectBase::isChance).forEach((effect) -> {
            effect.changeChance(2);
         });
      }

      return new int[]{power, accuracy};
   }
}

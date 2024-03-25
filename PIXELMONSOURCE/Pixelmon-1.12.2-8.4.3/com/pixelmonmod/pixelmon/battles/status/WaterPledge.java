package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class WaterPledge extends PledgeBase {
   public WaterPledge() {
      super(StatusType.WaterPledge);
   }

   public int[] modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      a.getMove().effects.stream().filter(EffectBase::isChance).forEach((effect) -> {
         effect.changeChance(2);
      });
      return new int[]{power, accuracy};
   }
}

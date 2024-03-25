package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Burn;

public class FlameBody extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (RandomHelper.getRandomChance(30) && Burn.burn(target, user, (Attack)null, false)) {
         user.bc.sendToAll("pixelmon.abilities.flamebody", target.getNickname(), user.getNickname());
      }

   }
}

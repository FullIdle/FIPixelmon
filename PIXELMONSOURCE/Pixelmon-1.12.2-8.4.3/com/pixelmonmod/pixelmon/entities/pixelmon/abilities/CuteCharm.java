package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Infatuated;

public class CuteCharm extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (RandomHelper.getRandomChance(30) && Infatuated.infatuate(target, user, false)) {
         user.bc.sendToAll("pixelmon.abilities.cutecharm", target.getNickname(), user.getNickname());
      }

   }
}

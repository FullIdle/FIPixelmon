package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;
import com.pixelmonmod.pixelmon.battles.status.Poison;
import com.pixelmonmod.pixelmon.battles.status.Sleep;

public class EffectSpore extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.isImmuneToPowder()) {
         int r = RandomHelper.getRandomNumberBetween(1, 100);
         if (r <= 9) {
            if (Poison.poison(target, user, (Attack)null, false)) {
               user.bc.sendToAll("pixelmon.abilities.effectsporepois", target.getNickname(), user.getNickname());
            }
         } else if (r <= 19) {
            if (Paralysis.paralyze(target, user, (Attack)null, false)) {
               user.bc.sendToAll("pixelmon.abilities.effectsporeparal", target.getNickname(), user.getNickname());
            }
         } else if (r <= 30 && Sleep.sleep(target, user, (Attack)null, false)) {
            user.bc.sendToAll("pixelmon.abilities.effectsporesleep", target.getNickname(), user.getNickname());
         }

      }
   }
}

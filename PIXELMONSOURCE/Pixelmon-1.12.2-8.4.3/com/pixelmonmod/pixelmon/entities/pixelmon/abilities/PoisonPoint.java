package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Poison;

public class PoisonPoint extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (RandomHelper.getRandomChance(30) && Poison.poison(target, user, (Attack)null, false)) {
         user.bc.sendToAll("pixelmon.abilities.poisonpoint", target.getNickname(), user.getNickname());
      }

   }
}

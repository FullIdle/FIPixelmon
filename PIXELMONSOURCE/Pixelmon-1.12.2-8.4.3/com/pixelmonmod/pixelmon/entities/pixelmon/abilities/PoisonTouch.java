package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Poison;

public class PoisonTouch extends AbilityBase {
   public void applyEffectOnContactUser(PixelmonWrapper user, PixelmonWrapper target) {
      if (RandomHelper.getRandomChance(0.3F) && Poison.poison(user, target, (Attack)null, false)) {
         user.bc.sendToAll("pixelmon.abilities.poisontouch", user.getNickname(), target.getNickname());
      }

   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sandstorm;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Weather;

public class ShoreUp extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasFullHealth()) {
         user.bc.sendToAll("pixelmon.effect.healfailed", user.getNickname());
         return AttackResult.failed;
      } else if (user.hasStatus(StatusType.HealBlock)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      } else {
         double heal = (double)((float)user.getMaxHealth() * this.getHealMultiplier(user));
         user.bc.sendToAll("pixelmon.effect.washealed", user.getNickname());
         user.healEntityBy((int)heal);
         return AttackResult.proceed;
      }
   }

   private float getHealMultiplier(PixelmonWrapper user) {
      Weather weather = user.bc.globalStatusController.getWeather();
      return weather instanceof Sandstorm ? 0.6666667F : 0.5F;
   }
}

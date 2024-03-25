package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class ZCurse extends EffectBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.hasType(EnumType.Ghost)) {
         if (user.getHealthPercent() != 100.0F) {
            user.healByPercent(100.0F);
            user.bc.sendToAll("pixelmon.effect.washealed", user.getNickname());
         }
      } else {
         (new StatsEffect(StatsType.Attack, 1, true)).applyStatEffect(user, user, user.attack.getMove());
      }

   }

   public boolean cantMiss(PixelmonWrapper user) {
      return true;
   }
}

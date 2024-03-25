package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class TanglingHair extends AbilityBase {
   public void applyEffectOnContactTarget(PixelmonWrapper user, PixelmonWrapper target) {
      if (user != target && user.getBattleStats().speedModifier > -6) {
         user.bc.sendToAll("pixelmon.abilities.tanglinghair", user.getNickname(), target.getNickname());
         user.getBattleStats().modifyStat(-1, StatsType.Speed, user, false);
         StatsEffect.addStatChangeAnimation(target, user, StatsType.Speed, -1);
      }

   }
}

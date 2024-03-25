package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class SteamEngine extends AbilityBase {
   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user != target && (a.getType() == EnumType.Water || a.getType() == EnumType.Fire) && target.getBattleStats().statCanBeRaised(StatsType.Speed)) {
         user.bc.sendToAll("pixelmon.abilities.steamengine", target.getNickname());
         int amountChanged = 6 - target.getBattleStats().getStage(StatsType.Speed);
         target.getBattleStats().modifyStat(6, StatsType.Speed, target, false);
         StatsEffect.addStatChangeAnimation(user, target, StatsType.Speed, amountChanged);
      }

   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class WaterCompaction extends AbilityBase {
   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user != target && a.getType() == EnumType.Water && target.getBattleStats().statCanBeRaised(StatsType.Defence)) {
         user.bc.sendToAll("pixelmon.abilities.watercompaction", target.getNickname());
         int amountChanged = target.getBattleStats().getStage(StatsType.Defence) == 5 ? 1 : 2;
         target.getBattleStats().modifyStat(2, StatsType.Defence, target, false);
         StatsEffect.addStatChangeAnimation(user, target, StatsType.Defence, amountChanged);
      }

   }
}

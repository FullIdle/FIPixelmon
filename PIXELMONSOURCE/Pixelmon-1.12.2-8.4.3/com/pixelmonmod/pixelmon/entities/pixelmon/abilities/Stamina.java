package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Stamina extends AbilityBase {
   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (user != target && target.getBattleStats().statCanBeRaised(StatsType.Defence)) {
         target.bc.sendToAll("pixelmon.abilities.stamina.activate", target.getNickname());
         (new StatsEffect(StatsType.Defence, 1, true)).applyStatEffect(target, target, (AttackBase)null);
      }

   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class WeakArmor extends AbilityBase {
   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper opponent, Attack a) {
      if (a.getAttackCategory() == AttackCategory.PHYSICAL && opponent.isAlive()) {
         this.sendActivatedMessage(opponent);
         opponent.getBattleStats().modifyStat(new int[]{-1, 2}, new StatsType[]{StatsType.Defence, StatsType.Speed});
      }

   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Rattled extends AbilityBase {
   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (target.isAlive() && (a.getType().equals(EnumType.Bug) || a.getType().equals(EnumType.Dark) || a.getType().equals(EnumType.Ghost))) {
         this.activate(target);
      }

   }

   public void activate(PixelmonWrapper target) {
      this.sendActivatedMessage(target);
      target.getBattleStats().modifyStat(1, (StatsType)StatsType.Speed);
   }
}

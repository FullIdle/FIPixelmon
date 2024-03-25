package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class AngerPoint extends AbilityBase {
   public boolean wasCrit = false;

   public void tookDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (this.wasCrit && target.isAlive()) {
         target.bc.sendToAll("pixelmon.abilities.angerpoint", target.getNickname());
         target.getBattleStats().modifyStat(12, (StatsType)StatsType.Attack);
      }

      this.wasCrit = false;
   }
}

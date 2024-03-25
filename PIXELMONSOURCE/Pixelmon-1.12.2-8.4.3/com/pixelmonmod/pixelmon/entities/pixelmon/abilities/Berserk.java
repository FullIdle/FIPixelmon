package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class Berserk extends AbilityBase {
   public void tookDamageTargetAfterMove(PixelmonWrapper user, PixelmonWrapper target, Attack a, float damage) {
      if (user.getBattleAbility().isAbility(SheerForce.class)) {
         SheerForce sheerForce = (SheerForce)user.getBattleAbility();
         if (sheerForce.powerModified) {
            return;
         }
      }

      float percentNow = target.getHealthPercent();
      float percentBefore = target.getHealthPercent((float)target.getHealth() + damage);
      if (percentBefore > 50.0F && percentNow <= 50.0F) {
         target.bc.sendToAll("pixelmon.abilities.berserk", target.getNickname());
         target.getBattleStats().modifyStat(1, (StatsType)StatsType.SpecialAttack);
      }

   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class GyroBall extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      user.attack.getMove().setBasePower((int)(25.0 * ((double)target.getBattleStats().getStatWithMod(StatsType.Speed) / (double)user.getBattleStats().getStatWithMod(StatsType.Speed))));
      if (user.attack.getMove().getBasePower() > 150) {
         user.attack.getMove().setBasePower(150);
      }

      return AttackResult.proceed;
   }
}

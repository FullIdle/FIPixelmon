package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class ElectroBall extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      int targetSpeed = target.getBattleStats().getStatWithMod(StatsType.Speed);
      int userSpeed = user.getBattleStats().getStatWithMod(StatsType.Speed);
      if (targetSpeed > userSpeed / 2) {
         user.attack.getMove().setBasePower(60);
      } else if (targetSpeed > userSpeed / 3) {
         user.attack.getMove().setBasePower(80);
      } else if (targetSpeed > userSpeed / 4) {
         user.attack.getMove().setBasePower(120);
      } else {
         user.attack.getMove().setBasePower(150);
      }

      return AttackResult.proceed;
   }
}

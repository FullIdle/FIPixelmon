package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class MeteorBeam extends MultiTurnCharge {
   public MeteorBeam() {
      super("pixelmon.effect.meteorbeam");
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.getTurnCount(user) == 0) {
         user.getBattleStats().modifyStat(1, (StatsType)StatsType.SpecialAttack);
      }

      return super.applyEffectDuring(user, target);
   }
}

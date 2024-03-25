package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn.MultiTurnSpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.Iterator;

public class RaiseStats extends SpecialAttackBase {
   public int stages = 1;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         if (user.attack.isAttack("Geomancy") && user.usingZ) {
            Iterator var3 = user.attack.getMove().effects.iterator();

            while(var3.hasNext()) {
               EffectBase effect = (EffectBase)var3.next();
               if (effect instanceof MultiTurnSpecialAttackBase && ((MultiTurnSpecialAttackBase)effect).getTurnCount(user) == 0) {
                  user.getBattleStats().modifyStat(this.stages, StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed);
               }
            }
         } else {
            user.getBattleStats().modifyStat(this.stages, StatsType.Attack, StatsType.Defence, StatsType.SpecialAttack, StatsType.SpecialDefence, StatsType.Speed);
         }
      }

      return AttackResult.proceed;
   }
}

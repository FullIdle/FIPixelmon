package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ParentalBond;

public class Psywave extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      int numHits = user.getBattleAbility() instanceof ParentalBond ? 2 : 1;

      for(int i = 0; i < numHits; ++i) {
         float random = RandomHelper.getRandomNumberBetween(0.5F, 1.5F);
         if (user.bc.simulateMode) {
            random = 1.0F;
         }

         float damage = random * (float)user.getLevelNum();
         target.doBattleDamage(user, damage, DamageTypeEnum.ATTACKFIXED);
         if (i == 1) {
            user.bc.sendToAll("multiplehit.times", user.getNickname(), 2);
         } else if (user.isFainted() || target.isFainted()) {
            break;
         }
      }

      return AttackResult.hit;
   }
}

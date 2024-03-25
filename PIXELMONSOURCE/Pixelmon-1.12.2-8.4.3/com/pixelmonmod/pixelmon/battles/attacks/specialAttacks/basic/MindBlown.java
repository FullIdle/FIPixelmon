package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Damp;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import java.util.Iterator;

public class MindBlown extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      Iterator var3 = user.bc.getActiveUnfaintedPokemon().iterator();

      PixelmonWrapper wrapper;
      do {
         if (!var3.hasNext()) {
            if (target.isFainted()) {
               return AttackResult.notarget;
            }

            if (user.isAlive() && !(user.getBattleAbility() instanceof MagicGuard)) {
               user.doBattleDamage(user, (float)Math.ceil((double)user.getMaxHealth() / 2.0), DamageTypeEnum.SELF);
            }

            return super.applyEffectDuring(user, target);
         }

         wrapper = (PixelmonWrapper)var3.next();
      } while(!(wrapper.getBattleAbility() instanceof Damp));

      if (target.getBattleAbility() instanceof Damp) {
         user.bc.sendToAll("pixelmon.abilities.damp", target.getNickname());
      }

      return AttackResult.failed;
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return false;
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ParentalBond;

public class DoSetDamage extends SpecialAttackBase {
   int damage;

   public DoSetDamage(Value... values) {
      this.damage = values[0].value;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      boolean hasParentalBond = user.getBattleAbility() instanceof ParentalBond;
      if (this.damage != 0) {
         target.doBattleDamage(user, (float)this.damage, DamageTypeEnum.ATTACKFIXED);
         if (hasParentalBond && user.isAlive() && target.isAlive()) {
            target.doBattleDamage(user, (float)this.damage, DamageTypeEnum.ATTACKFIXED);
            user.bc.sendToAll("multiplehit.times", user.getNickname(), 2);
         }
      } else {
         int newDamage = target.getHealth(true) / 2;
         target.doBattleDamage(user, (float)newDamage, DamageTypeEnum.ATTACKFIXED);
         if (hasParentalBond && user.isAlive() && target.isAlive()) {
            newDamage = target.getHealth(true) / 2;
            target.doBattleDamage(user, (float)newDamage, DamageTypeEnum.ATTACKFIXED);
            user.bc.sendToAll("multiplehit.times", user.getNickname(), 2);
         }
      }

      return AttackResult.hit;
   }
}

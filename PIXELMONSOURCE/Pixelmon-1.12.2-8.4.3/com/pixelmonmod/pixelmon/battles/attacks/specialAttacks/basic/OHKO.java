package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class OHKO extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.attack.isAttack("Sheer Cold") && target.hasType(EnumType.Ice)) {
         user.bc.sendToAll("pixelmon.status.noeffect");
         return AttackResult.failed;
      } else {
         int targetLevel = target.getLevelNum();
         int userLevel = user.getLevelNum();
         int chance;
         if (targetLevel > userLevel) {
            chance = 0;
         } else {
            if (user.attack.moveAccuracy == -2) {
               user.attack.moveResult.accuracy = -2;
               return this.doOHKO(user, target);
            }

            chance = Math.min(userLevel - targetLevel + 30, 100);
         }

         user.attack.moveResult.accuracy = chance;
         if (chance == 0) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         } else if (!user.bc.simulateMode && !RandomHelper.getRandomChance(chance)) {
            user.bc.sendToAll("pixelmon.battletext.missedattack", target.getNickname());
            return AttackResult.failed;
         } else {
            return this.doOHKO(user, target);
         }
      }
   }

   private AttackResult doOHKO(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.effect.OHKO");
      int damage = target.getHealth();
      user.attack.moveResult.damage = (int)target.doBattleDamage(user, (float)damage, DamageTypeEnum.ATTACKFIXED);
      user.attack.moveResult.fullDamage = target.getMaxHealth();
      return AttackResult.hit;
   }
}

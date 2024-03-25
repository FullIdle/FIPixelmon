package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SkillLink;

public class TripleAxel extends SpecialAttackBase {
   private transient int count = 0;

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      boolean hasSubstitute = false;
      if (this.count != 0) {
         return AttackResult.proceed;
      } else {
         boolean missed = false;
         user.inMultipleHit = true;

         while(this.count < 3 && user.isAlive() && target.isAlive() && !missed) {
            ++this.count;
            hasSubstitute = target.hasStatus(StatusType.Substitute);
            MoveResults[] results = user.useAttackOnly();
            MoveResults[] var6 = results;
            int var7 = results.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               MoveResults result = var6[var8];
               if (result.result == AttackResult.failed || result.result == AttackResult.missed) {
                  --this.count;
                  missed = true;
               }

               MoveResults var10000 = user.attack.moveResult;
               var10000.damage += result.damage;
               var10000 = user.attack.moveResult;
               var10000.fullDamage += result.fullDamage;
               user.attack.moveResult.accuracy = result.accuracy;
            }

            user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() + 20);
            if (user.getBattleAbility() instanceof SkillLink) {
               user.attack.getMove().setAccuracy(-1);
            }
         }

         user.inMultipleHit = false;
         user.attack.playAnimation(user, target);
         if (this.count > 0) {
            user.attack.sendEffectiveChat(user, target);
            user.bc.sendToAll("multiplehit.times", user.getNickname(), this.count);
         }

         this.count = 0;
         user.attack.getMove().setBasePower(20);
         user.attack.savedPower = 20;
         user.attack.getMove().setAccuracy(90);
         Attack.postProcessAttackAllHits(user, target, user.attack, (float)user.attack.moveResult.damage, DamageTypeEnum.ATTACK, hasSubstitute);
         if (!hasSubstitute) {
            Attack.applyContactLate(user, target);
         }

         return AttackResult.hit;
      }
   }
}

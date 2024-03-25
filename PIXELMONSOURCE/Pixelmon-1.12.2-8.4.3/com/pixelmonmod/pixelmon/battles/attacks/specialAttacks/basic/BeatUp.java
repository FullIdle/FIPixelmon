package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class BeatUp extends SpecialAttackBase {
   private transient boolean inProgress = false;
   private transient int limit = 0;

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.limit++ < 6 && this.inProgress) {
         return AttackResult.proceed;
      } else {
         user.inMultipleHit = true;
         this.inProgress = true;
         this.limit = 0;
         boolean hasSubstitute = false;
         boolean firstHit = true;
         PixelmonWrapper[] var5 = user.getParticipant().allPokemon;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            PixelmonWrapper p = var5[var7];
            if (p.isAlive() && !p.hasPrimaryStatus()) {
               if (!firstHit) {
                  user.attack.getMove().setAccuracy(-1);
               }

               hasSubstitute = target.hasStatus(StatusType.Substitute);
               this.doAttack(user, target, p);
               firstHit = false;
            }
         }

         user.inMultipleHit = false;
         this.inProgress = false;
         user.attack.sendEffectiveChat(user, target);
         Attack.postProcessAttackAllHits(user, target, user.attack, (float)user.attack.moveResult.damage, DamageTypeEnum.ATTACK, hasSubstitute);
         if (!hasSubstitute) {
            Attack.applyContactLate(user, target);
         }

         return AttackResult.hit;
      }
   }

   private void doAttack(PixelmonWrapper user, PixelmonWrapper target, PixelmonWrapper currentAttacker) {
      user.attack.getMove().setBasePower(currentAttacker.getBaseStats().getStat(StatsType.Attack) / 10 + 5);
      MoveResults[] results = user.useAttackOnly();
      MoveResults[] var5 = results;
      int var6 = results.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         MoveResults result = var5[var7];
         MoveResults var10000 = user.attack.moveResult;
         var10000.damage += result.damage;
         var10000 = user.attack.moveResult;
         var10000.fullDamage += result.fullDamage;
      }

   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.RaidPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.BattleBond;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SkillLink;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;

public class MultipleHit extends AttackModifierBase {
   private transient int count = 0;
   private transient int targetCount = 0;
   private transient boolean inProgress = false;
   private transient int limit = 0;
   public int minHits = 2;
   public int maxHits = 5;

   public MultipleHit(Value... values) {
      this.minHits = values[0].value;
      this.maxHits = values[1].value;
   }

   public boolean repeatsAttack() {
      if (this.targetCount == 1) {
         if (this.count == 0) {
            ++this.count;
            return true;
         } else {
            return false;
         }
      } else {
         if (this.maxHits == 0) {
            if (this.count >= this.minHits) {
               return false;
            }
         } else if (this.count >= this.targetCount) {
            return false;
         }

         ++this.count;
         return true;
      }
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.limit++ <= 5 && this.inProgress) {
         return AttackResult.proceed;
      } else {
         user.inMultipleHit = true;
         this.inProgress = true;
         this.limit = 0;
         int random;
         if (this.maxHits != 0) {
            if (this.minHits == 2 && this.maxHits == 5) {
               random = RandomHelper.getRandomNumberBetween(0, 9);
               if (user.bc.simulateMode) {
                  this.targetCount = 1;
               } else if (random < 3) {
                  this.targetCount = 2;
               } else if (random < 6) {
                  this.targetCount = 3;
               } else if (random < 8) {
                  this.targetCount = 4;
               } else {
                  this.targetCount = 5;
               }
            } else {
               this.targetCount = RandomHelper.getRandomNumberBetween(this.minHits, this.maxHits);
            }
         }

         if (user.getBattleAbility() instanceof SkillLink) {
            this.targetCount = this.maxHits;
         } else if (user.getBattleAbility() instanceof BattleBond && user.attack.isAttack("Water Shuriken") && (user.getForm() == EnumGreninja.ASH.getForm() || user.getForm() == EnumGreninja.ASH_ZOMBIE.getForm() || user.getForm() == EnumGreninja.ASH_ALTER.getForm())) {
            this.targetCount = 3;
         }

         if (target.isRaidPokemon()) {
            RaidPixelmonParticipant rpp = (RaidPixelmonParticipant)target.getParticipant();
            if (rpp.areShieldsUp()) {
               this.targetCount = 1;
            }
         }

         this.count = 0;
         random = user.attack.getMove().getAccuracy();
         boolean hasSubstitute = false;

         MoveResults var10000;
         while(target.isAlive() && user.isAlive() && this.repeatsAttack()) {
            user.attack.getMove().setAccuracy(-1);
            hasSubstitute = target.hasStatus(StatusType.Substitute);
            MoveResults[] results = user.useAttackOnly();
            MoveResults[] var6 = results;
            int var7 = results.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               MoveResults result = var6[var8];
               var10000 = user.attack.moveResult;
               var10000.damage += result.damage;
               var10000 = user.attack.moveResult;
               var10000.fullDamage += result.fullDamage;
            }
         }

         user.attack.getMove().setAccuracy(random);
         if (user.bc.simulateMode && this.targetCount == 1) {
            user.attack.moveResult.damage = (int)Math.min((double)user.attack.moveResult.damage * 3.168, (double)target.getHealth());
            var10000 = user.attack.moveResult;
            var10000.fullDamage = (int)((double)var10000.fullDamage * 3.168);
         }

         user.inMultipleHit = false;
         this.inProgress = false;
         user.attack.sendEffectiveChat(user, target);
         if (this.count > 1) {
            user.bc.sendToAll("multiplehit.times", user.getNickname(), this.count);
         }

         Attack.postProcessAttackAllHits(user, target, user.attack, (float)user.attack.moveResult.damage, DamageTypeEnum.ATTACK, hasSubstitute);
         if (!hasSubstitute) {
            Attack.applyContactLate(user, target);
         }

         return AttackResult.hit;
      }
   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ParentalBond;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class DamageReflect extends SpecialAttackBase {
   public float multiplier;
   private transient boolean inProgress;

   public DamageReflect(float multiplier) {
      this.multiplier = multiplier;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.inProgress) {
         this.inProgress = false;
         return AttackResult.proceed;
      } else if (user.bc.simulateMode) {
         return AttackResult.hit;
      } else {
         List opponents = user.getOpponentPokemon();
         if (user.lastDirectDamage >= 0 && this.isCorrectCategory(user.lastDirectCategory) && !opponents.isEmpty()) {
            if (opponents.size() <= user.lastDirectPosition) {
               user.lastDirectPosition = 0;
            }

            target = (PixelmonWrapper)opponents.get(user.lastDirectPosition);
            if (user.attack.getMove().isAttack("Mirror Coat") && target.hasType(EnumType.Dark)) {
               user.bc.sendToAll("pixelmon.effect.effectfailed", user.getNickname());
               return AttackResult.failed;
            } else if (user.attack.getMove().isAttack("Counter") && target.hasType(EnumType.Ghost)) {
               user.bc.sendToAll("pixelmon.effect.effectfailed", user.getNickname());
               return AttackResult.failed;
            } else if (user.lastDirectDamage == 0) {
               user.attack.getMove().setBasePower(1);
               this.inProgress = true;
               user.useAttackOnly();
               return AttackResult.proceed;
            } else {
               float damage = (float)user.lastDirectDamage * this.multiplier;
               target.doBattleDamage(user, damage, DamageTypeEnum.ATTACKFIXED);
               if (target.isAlive() && user.isAlive() && user.getBattleAbility() instanceof ParentalBond) {
                  target.doBattleDamage(user, damage, DamageTypeEnum.ATTACKFIXED);
                  user.bc.sendToAll("multiplehit.times", user.getNickname(), 2);
               }

               return AttackResult.hit;
            }
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      }
   }

   public abstract boolean isCorrectCategory(AttackCategory var1);

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      MoveChoice reflectChoice = null;
      Iterator var8 = bestOpponentChoices.iterator();

      while(var8.hasNext()) {
         MoveChoice opponentChoice = (MoveChoice)var8.next();
         if (opponentChoice.isOffensiveMove() && this.isCorrectCategory(opponentChoice.attack.getAttackCategory()) && opponentChoice.targets.contains(pw) && MoveChoice.canOutspeed(opponentChoice.createList(), pw, userChoice.createList())) {
            reflectChoice = opponentChoice;
            break;
         }
      }

      if (reflectChoice != null) {
         var8 = userChoice.targets.iterator();

         while(var8.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var8.next();
            float reflectDamage = (float)(Math.floor((double)(reflectChoice.result.damage / reflectChoice.targets.size())) * (double)this.multiplier);
            userChoice.raiseWeightLimited(target.getHealthPercent(reflectDamage));
         }
      }

      if (userChoice.isMiddleTier() && RandomHelper.getRandomChance()) {
         userChoice.setWeight(1.0F);
      }

   }
}

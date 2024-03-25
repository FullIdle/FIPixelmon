package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.BideStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;

public class Bide extends MultiTurnSpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.simulateMode) {
         return AttackResult.hit;
      } else {
         if (!this.doesPersist(user)) {
            this.setPersists(user, true);
            this.setTurnCount(user, 3);
         }

         this.decrementTurnCount(user);
         int turnCount = this.getTurnCount(user);
         if (turnCount == 2) {
            user.bc.sendToAll("pixelmon.effect.bidetime", user.getNickname());
            user.addStatus(new BideStatus(), user);
            return AttackResult.charging;
         } else if (turnCount == 1) {
            user.bc.sendToAll("pixelmon.effect.storeenergy", user.getNickname());
            return AttackResult.charging;
         } else if (turnCount <= 0) {
            this.setPersists(user, false);
            user.bc.sendToAll("pixelmon.effect.unleashenergy", user.getNickname());
            BideStatus status = (BideStatus)user.getStatus(StatusType.Bide);
            float damage = 0.0F;
            if (status != null) {
               damage = status.damageTaken * 2.0F;
            }

            if (damage != 0.0F && status != null) {
               if (status.lastAttacker.getEffectiveTypes(user, status.lastAttacker).contains(EnumType.Ghost)) {
                  user.bc.sendToAll("pixelmon.battletext.noeffect", status.lastAttacker.getNickname());
                  user.removeStatus(StatusType.Bide);
                  return AttackResult.failed;
               } else {
                  ArrayList activePokemon = status.lastAttacker.getParticipant().getActiveUnfaintedPokemon();
                  if (activePokemon != null && activePokemon.size() > 0) {
                     Iterator var7 = ((PixelmonWrapper)activePokemon.get(0)).getStatuses().iterator();

                     while(var7.hasNext()) {
                        StatusBase e = (StatusBase)var7.next();

                        try {
                           if (e.stopsIncomingAttack(target, user)) {
                              user.bc.sendToAll("pixelmon.effect.redaying", status.lastAttacker.getNickname());
                              user.removeStatus(StatusType.Bide);
                              return AttackResult.failed;
                           }
                        } catch (Exception var10) {
                           user.bc.battleLog.onCrash(var10, "Error calculating stopsIncomingAttack for " + e.type.toString() + " for attack Bide");
                        }
                     }

                     ((PixelmonWrapper)activePokemon.get(0)).doBattleDamage(user, damage, DamageTypeEnum.ATTACKFIXED);
                     user.removeStatus(StatusType.Bide);
                     return AttackResult.hit;
                  } else {
                     user.removeStatus(StatusType.Bide);
                     return AttackResult.failed;
                  }
               }
            } else {
               user.bc.sendToAll("pixelmon.effect.effectfailed");
               user.removeStatus(StatusType.Bide);
               return AttackResult.failed;
            }
         } else {
            return AttackResult.failed;
         }
      }
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return true;
   }

   public boolean ignoresType(PixelmonWrapper user) {
      return true;
   }

   public void removeEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.setPersists(user, false);
   }

   public boolean isCharging(PixelmonWrapper user, PixelmonWrapper target) {
      return !this.doesPersist(user) || this.getTurnCount(user) > 1;
   }

   public boolean shouldNotLosePP(PixelmonWrapper user) {
      return this.doesPersist(user);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      PixelmonWrapper lastMover = pw.bc.getLastMover(pw.getOpponentPokemon());
      if (lastMover != null) {
         float maxDamagePercent = MoveChoice.getMaxDamagePercent(pw, MoveChoice.getTargetedChoices(pw, bestOpponentChoices));
         maxDamagePercent = maxDamagePercent / (float)pw.getMaxHealth() * (float)lastMover.getMaxHealth();
         userChoice.raiseWeight(maxDamagePercent * 2.0F / 3.0F);
      }

      if (MoveChoice.canOutspeedAndKO(3, bestOpponentChoices, pw, userChoice.createList())) {
         userChoice.lowerTier(1);
      }

   }
}

package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.multiTurn;

import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.MultiTurn;
import com.pixelmonmod.pixelmon.battles.status.StatusType;

public abstract class MultiTurnSpecialAttackBase extends EffectBase {
   public MultiTurnSpecialAttackBase() {
      super(true);
   }

   private MultiTurn getMultiTurnStatus(PixelmonWrapper user) {
      return (MultiTurn)user.getStatus(StatusType.MultiTurn);
   }

   public int getTurnCount(PixelmonWrapper user) {
      MultiTurn multiTurn = this.getMultiTurnStatus(user);
      return multiTurn == null ? 0 : multiTurn.numTurns;
   }

   public void decrementTurnCount(PixelmonWrapper user) {
      if (!user.bc.simulateMode) {
         MultiTurn multiTurn = this.getMultiTurnStatus(user);
         if (multiTurn != null) {
            --multiTurn.numTurns;
         }
      }

   }

   public void setTurnCount(PixelmonWrapper user, int value) {
      if (!user.bc.simulateMode) {
         MultiTurn multiTurn = this.getMultiTurnStatus(user);
         if (multiTurn != null) {
            multiTurn.numTurns = value;
         }
      }

   }

   public boolean doesPersist(PixelmonWrapper user) {
      return this.getMultiTurnStatus(user) != null;
   }

   protected void setPersists(PixelmonWrapper user, boolean value) {
      if (!user.bc.simulateMode) {
         if (value) {
            user.addStatus(new MultiTurn(), user);
         } else {
            user.removeStatus(StatusType.MultiTurn);
         }
      }

   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      return AttackResult.proceed;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public boolean cantMiss(PixelmonWrapper user) {
      return false;
   }

   public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
      this.removeEffect(user, target);
   }

   public boolean ignoresType(PixelmonWrapper user) {
      return false;
   }

   public void removeEffect(PixelmonWrapper user, PixelmonWrapper target) {
   }

   public boolean isCharging(PixelmonWrapper user, PixelmonWrapper target) {
      return false;
   }

   public boolean shouldNotLosePP(PixelmonWrapper user) {
      return false;
   }
}

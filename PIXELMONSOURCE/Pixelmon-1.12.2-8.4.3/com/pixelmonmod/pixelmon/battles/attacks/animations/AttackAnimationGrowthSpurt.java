package com.pixelmonmod.pixelmon.battles.attacks.animations;

import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.HashMap;

public class AttackAnimationGrowthSpurt extends AttackAnimation {
   public HashMap effects = new HashMap();
   private transient HashMap remainingEffects = null;
   public boolean groundedStartPosition = false;
   public boolean groundedEndPosition = false;
   transient float currentScale;
   public int growthIncrement = 0;
   public int growthQuantity = 0;
   public boolean returnToOriginalSize = false;

   public void initialize(PixelmonWrapper user, PixelmonWrapper target, Attack attack) {
      super.initialize(user, target, attack);
      this.remainingEffects = new HashMap(this.effects);
   }

   public boolean tickAnimation(int tick) {
      if (this.growthIncrement != 0 && this.growthQuantity != 0) {
         if (this.remainingEffects.containsKey(tick)) {
            AttackAnimationData effect = (AttackAnimationData)this.remainingEffects.remove(tick);
            this.sendBattleEffect(effect, this.groundedStartPosition, this.groundedEndPosition);
         }

         if (tick == 0 && this.growthQuantity > 0) {
            this.adjustScale();
         }

         if (tick == 5 && this.growthQuantity > 1) {
            this.adjustScale();
         }

         if (tick == 10 && this.growthQuantity > 2) {
            this.adjustScale();
         }

         if (tick == 15 && this.growthQuantity > 3) {
            this.adjustScale();
         }

         if (tick == 20 && this.growthQuantity > 4) {
            this.adjustScale();
         }

         if (!this.returnToOriginalSize || (tick != 25 || this.growthQuantity != 1) && (tick != 30 || this.growthQuantity != 2) && (tick != 35 || this.growthQuantity != 3) && (tick != 40 || this.growthQuantity != 4) && (tick != 45 || this.growthQuantity <= 4)) {
            return this.remainingEffects.isEmpty() && tick >= 45;
         } else {
            this.user.entity.setPixelmonScale(1.0F);
            return false;
         }
      } else {
         return true;
      }
   }

   public boolean adjustScale() {
      this.currentScale = this.user.entity.getPixelmonScale();
      int i;
      if (this.growthIncrement < 0) {
         for(i = 0; i > this.growthIncrement; --i) {
            this.currentScale *= 0.9F;
         }
      } else if (this.growthIncrement > 0) {
         for(i = 0; i < this.growthIncrement; ++i) {
            this.currentScale *= 1.1F;
         }
      }

      this.user.entity.setPixelmonScale(this.currentScale);
      return false;
   }

   public boolean usedOncePerTurn() {
      return true;
   }

   public int getAdjustmentIncrement() {
      return this.growthIncrement;
   }

   public int getAdjustmentQuantity() {
      return this.growthQuantity;
   }

   public boolean getReturnToOriginalSize() {
      return this.returnToOriginalSize;
   }

   public AttackAnimationGrowthSpurt setAdjustmentIncrement(int growthIncrement) {
      this.growthIncrement = growthIncrement;
      return this;
   }

   public AttackAnimationGrowthSpurt setAdjustmentQuantity(int growthQuantity) {
      this.growthQuantity = growthQuantity;
      return this;
   }

   public AttackAnimationGrowthSpurt setReturnToOriginalSize(boolean returnToOriginalSize) {
      this.returnToOriginalSize = returnToOriginalSize;
      return this;
   }
}

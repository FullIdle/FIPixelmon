package com.pixelmonmod.pixelmon.battles.attacks.animations;

import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.util.helpers.EntityHelper;

public class AttackAnimationVerticalStomp extends AttackAnimation {
   transient double originalY = 0.0;
   transient boolean onTheWayDown = false;
   transient int tickParticlesFired = 0;
   transient int ticksSinceLanding = 0;
   public float jumpPower = 0.0F;
   public float landPower = 0.0F;
   public AttackAnimationData landingEffect;

   public boolean tickAnimation(int tick) {
      if (tick == 0) {
         this.originalY = this.user.entity.field_70163_u;
         EntityHelper.setVelocity(this.user.entity, 0.0, this.jumpPower == 0.0F ? Math.sqrt((double)this.attack.getMove().getBasePower()) * 0.10000000149011612 : (double)this.jumpPower, 0.0);
         return false;
      } else if (this.onTheWayDown || (tick <= 0 || !(this.user.entity.field_70181_x < 0.1)) && tick <= 50) {
         if (this.tickParticlesFired != 0 || tick <= 40 && (!this.onTheWayDown || !(this.user.entity.field_70163_u <= this.originalY))) {
            return this.onTheWayDown && this.tickParticlesFired != 0 && this.ticks > this.tickParticlesFired + 20;
         } else if (this.landingEffect == null) {
            return true;
         } else {
            ++this.ticksSinceLanding;
            if (this.ticksSinceLanding > 3) {
               this.sendBattleEffect(this.landingEffect, true, true);
               this.tickParticlesFired = this.ticks;
            }

            return false;
         }
      } else {
         EntityHelper.setVelocity(this.user.entity, 0.0, this.landPower == 0.0F ? Math.sqrt((double)this.attack.getMove().getBasePower()) * -0.4000000059604645 : (double)this.landPower, 0.0);
         this.onTheWayDown = true;
         return false;
      }
   }

   public boolean usedOncePerTurn() {
      return true;
   }
}

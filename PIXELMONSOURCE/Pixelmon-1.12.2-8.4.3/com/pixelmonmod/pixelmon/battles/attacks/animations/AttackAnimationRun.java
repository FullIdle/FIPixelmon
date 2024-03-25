package com.pixelmonmod.pixelmon.battles.attacks.animations;

import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;

public class AttackAnimationRun extends AttackAnimation {
   public AttackAnimationData effect = null;
   public double originalUserX;
   public double originalUserY;
   public double originalUserZ;
   public double originalTargetX;
   public double originalTargetY;
   public double originalTargetZ;
   public double xDistance;
   public double zDistance;
   public double maxUserVelocity = 3.0;
   public double userVelocityX;
   public double userVelocityZ;
   public double targetVelocityX;
   public double targetVelocityZ;
   public double accelerationX;
   public double accelerationZ;
   public double initialVelocity = 0.0;
   public double acceleration = -1000.0;
   public double inertiaDistance;
   public double damagePercent;
   public boolean targetReached = false;
   public boolean targetInMotion = false;
   public boolean knockback = true;
   public boolean returnToStart = true;

   public boolean tickAnimation(int tick) {
      if (this.user == this.target) {
         return true;
      } else if (tick == 0) {
         this.originalUserX = this.user.entity.field_70165_t;
         this.originalUserY = this.user.entity.field_70163_u;
         this.originalUserZ = this.user.entity.field_70161_v;
         this.originalTargetX = this.target.entity.field_70165_t;
         this.originalTargetY = this.target.entity.field_70163_u;
         this.originalTargetZ = this.target.entity.field_70161_v;
         this.xDistance = this.target.entity.field_70165_t - this.user.entity.field_70165_t;
         this.zDistance = this.target.entity.field_70161_v - this.user.entity.field_70161_v;
         if (this.initialVelocity > 0.0) {
            this.userVelocityX = this.xDistance / (10.0 / this.initialVelocity);
            this.userVelocityZ = this.zDistance / (10.0 / this.initialVelocity);
         } else {
            this.userVelocityX = 0.0;
            this.userVelocityZ = 0.0;
         }

         if (this.acceleration == -1000.0) {
            this.acceleration = (double)(this.attack.movePower / 40);
         }

         this.accelerationX = this.xDistance / (100.0 / this.acceleration);
         this.accelerationZ = this.zDistance / (100.0 / this.acceleration);
         if (this.knockback) {
            this.inertiaDistance = 1.0;
         } else {
            this.inertiaDistance = 1.5;
         }

         if (this.effect != null) {
            this.sendBattleEffect(this.effect, false, false);
         }

         return false;
      } else {
         if (tick == 1) {
            this.damagePercent = (double)this.user.attack.moveResult.damage / (double)this.target.pokemon.getMaxHealth();
         }

         if (tick >= 35) {
            if (tick == 45 && this.returnToStart) {
               this.user.entity.func_82142_c(true);
               if (this.knockback) {
                  this.target.entity.func_82142_c(true);
               }

               this.user.entity.func_70107_b(this.originalUserX, this.originalUserY, this.originalUserZ);
               this.target.entity.func_70107_b(this.originalTargetX, this.originalTargetY, this.originalTargetZ);
               return false;
            } else if (tick == 50 && this.returnToStart) {
               this.user.entity.func_82142_c(false);
               if (this.knockback) {
                  this.target.entity.func_82142_c(false);
               }

               return false;
            } else {
               return tick >= 60;
            }
         } else {
            double userNextVelocityX = this.userVelocityX + this.accelerationX;
            double userNextVelocityZ = this.userVelocityZ + this.accelerationZ;
            double userNextVelocity = Math.sqrt(userNextVelocityX * userNextVelocityX + userNextVelocityZ * userNextVelocityZ);
            if (userNextVelocity < this.maxUserVelocity && !this.targetReached) {
               this.userVelocityX += this.accelerationX;
               this.userVelocityZ += this.accelerationZ;
            } else if (this.targetInMotion && this.knockback) {
               if (Math.signum(this.targetVelocityX) == Math.signum(this.targetVelocityX - this.accelerationX * (double)(tick / 5)) && Math.signum(this.targetVelocityZ) == Math.signum(this.targetVelocityZ - this.accelerationZ * (double)(tick / 5))) {
                  this.targetVelocityX -= this.accelerationX * (double)(tick / 5);
                  this.targetVelocityZ -= this.accelerationZ * (double)(tick / 5);
               } else {
                  this.targetVelocityX = 0.0;
                  this.targetVelocityZ = 0.0;
               }

               if (Math.signum(this.userVelocityX) == Math.signum(this.userVelocityX - this.accelerationX / 10.0) && Math.signum(this.userVelocityZ) == Math.signum(this.userVelocityZ - this.accelerationZ / 10.0)) {
                  this.userVelocityX -= this.accelerationX;
                  this.userVelocityZ -= this.accelerationZ;
               } else {
                  this.userVelocityX = 0.0;
                  this.userVelocityZ = 0.0;
               }
            }

            double xDistanceTraveled = this.user.entity.field_70165_t - this.originalUserX;
            double zDistanceTraveled = this.user.entity.field_70161_v - this.originalUserZ;
            double distanceTraveled = Math.sqrt(xDistanceTraveled * xDistanceTraveled + zDistanceTraveled * zDistanceTraveled);
            double distance = Math.sqrt(this.xDistance * this.xDistance + this.zDistance * this.zDistance);
            if (distanceTraveled < distance * this.inertiaDistance && !this.targetReached) {
               this.user.entity.field_70159_w = this.userVelocityX;
               this.user.entity.field_70179_y = this.userVelocityZ;
            } else if (this.knockback) {
               this.user.entity.field_70159_w = 0.0;
               this.user.entity.field_70179_y = 0.0;
               this.targetReached = true;
            } else {
               this.user.entity.field_70159_w = 0.0;
               this.user.entity.field_70179_y = 0.0;
               this.targetReached = true;
               this.user.entity.func_82142_c(true);
               this.user.entity.func_70107_b(this.originalUserX, this.originalUserY, this.originalUserZ);
            }

            if (distanceTraveled >= distance && this.knockback && !this.targetInMotion) {
               this.targetInMotion = true;
               this.targetVelocityX = this.userVelocityX * 2.0 * this.damagePercent;
               this.targetVelocityZ = this.userVelocityZ * 2.0 * this.damagePercent;
            }

            if (this.targetInMotion && this.knockback) {
               if (Math.abs(this.target.entity.field_70165_t - this.originalTargetX) < Math.abs(this.xDistance * 2.0 * this.damagePercent) && Math.abs(this.target.entity.field_70161_v - this.originalTargetZ) < Math.abs(this.zDistance * 2.0 * this.damagePercent)) {
                  this.target.entity.field_70159_w = this.targetVelocityX;
                  this.target.entity.field_70179_y = this.targetVelocityZ;
               } else {
                  this.target.entity.field_70159_w = 0.0;
                  this.target.entity.field_70179_y = 0.0;
               }
            }

            return false;
         }
      }
   }

   public boolean usedOncePerTurn() {
      return true;
   }
}

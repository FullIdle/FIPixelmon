package com.pixelmonmod.pixelmon.battles.attacks.animations;

import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.util.math.MathHelper;

public class AttackAnimationLeapForward extends AttackAnimation {
   public AttackAnimationData effect = null;

   public boolean tickAnimation(int tick) {
      if (this.user == this.target) {
         return true;
      } else {
         EntityPixelmon var10000;
         double d;
         double d1;
         float f;
         if (tick == 0) {
            d = this.target.entity.field_70165_t - this.user.entity.field_70165_t;
            d1 = this.target.entity.field_70161_v - this.user.entity.field_70161_v;
            f = MathHelper.func_76133_a(d * d + d1 * d1);
            var10000 = this.user.entity;
            var10000.field_70159_w += d / (double)f * 0.5 * 0.800000011920929 + this.user.entity.field_70159_w * 0.20000000298023224;
            var10000 = this.user.entity;
            var10000.field_70179_y += d1 / (double)f * 0.5 * 0.800000011920929 + this.user.entity.field_70179_y * 0.20000000298023224;
            this.user.entity.field_70181_x = 0.4;
            if (this.effect != null) {
               this.sendBattleEffect(this.effect, false, false);
            }

            return false;
         } else if (tick == 13) {
            d = this.target.entity.field_70165_t - this.user.entity.field_70165_t;
            d1 = this.target.entity.field_70161_v - this.user.entity.field_70161_v;
            f = MathHelper.func_76133_a(d * d + d1 * d1);
            var10000 = this.user.entity;
            var10000.field_70159_w -= d / (double)f * 0.5 * 0.800000011920929 + this.user.entity.field_70159_w * 0.20000000298023224;
            var10000 = this.user.entity;
            var10000.field_70179_y -= d1 / (double)f * 0.5 * 0.800000011920929 + this.user.entity.field_70179_y * 0.20000000298023224;
            this.user.entity.field_70181_x = 0.4;
            return false;
         } else {
            return tick >= 26;
         }
      }
   }

   public boolean usedOncePerTurn() {
      return true;
   }
}

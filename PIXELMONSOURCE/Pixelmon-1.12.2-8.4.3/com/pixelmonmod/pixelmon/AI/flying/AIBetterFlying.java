package com.pixelmonmod.pixelmon.AI.flying;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class AIBetterFlying extends EntityAIBase {
   EntityPixelmon pokemon;
   FlySpeeds flyingParameters;
   boolean wantsToFly = true;
   int actionTicks = -1;
   BlockPos target = null;
   float lastYaw;
   BlockPos.MutableBlockPos reusablePos = new BlockPos.MutableBlockPos(0, 0, 0);

   public AIBetterFlying(EntityPixelmon pokemon, FlySpeeds flyingParameters) {
      this.pokemon = pokemon;
      this.lastYaw = pokemon.field_70177_z;
      this.flyingParameters = flyingParameters;
   }

   public AIBetterFlying(EntityPixelmon pokemon) {
      this.pokemon = pokemon;
      this.flyingParameters = new FlySpeeds();
   }

   public void func_75249_e() {
      this.func_75248_a(1);
      if (this.pokemon.func_180425_c().func_177956_o() - this.pokemon.func_130014_f_().func_175672_r(this.pokemon.func_180425_c()).func_177956_o() > 10) {
         this.wantsToFly = true;
         this.actionTicks = 1;
         this.func_75250_a();
      }

   }

   public boolean func_75250_a() {
      --this.actionTicks;
      if (this.wantsToFly) {
         if (this.target == null && this.actionTicks <= 0) {
            this.findAerialTarget();
            if (this.target != null) {
               this.actionTicks = (int)((double)this.flyingParameters.endurance * (((double)this.pokemon.func_70681_au().nextFloat() - 0.5) / 5.0));
               this.moveToTarget();
            }
         } else if (this.target != null) {
            this.moveToTarget();
         }

         if (this.reachedTarget()) {
            this.findAerialTarget();
            if (this.target != null) {
               this.moveToTarget();
            }

            return true;
         }

         if (this.actionTicks <= 0) {
            this.target = this.findLanding();
            if (this.target != null) {
               this.wantsToFly = false;
               this.actionTicks = this.flyingParameters.endurance / 3;
               this.moveToTarget();
            }
         }
      } else {
         if (this.target != null && this.target.func_177951_i(this.pokemon.func_180425_c()) > 4.0) {
            this.moveToTarget();
         }

         if (this.actionTicks <= 0) {
            this.wantsToFly = true;
            this.findAerialTarget();
         }
      }

      return false;
   }

   private double findAngle(double x, double z) {
      if (x >= 0.0 && z >= 0.0) {
         return MathHelper.func_181159_b(x, z);
      } else if (x <= 0.0 && z >= 0.0) {
         return Math.PI - MathHelper.func_181159_b(x, z);
      } else if (x <= 0.0 && z <= 0.0) {
         return Math.PI + MathHelper.func_181159_b(x, z);
      } else {
         return x >= 0.0 && z <= 0.0 ? 6.283185307179586 - MathHelper.func_181159_b(x, z) : 0.0;
      }
   }

   public void moveToTarget() {
      double diffX = (double)this.target.func_177958_n() - this.pokemon.func_174791_d().field_72450_a;
      double diffZ = (double)this.target.func_177952_p() - this.pokemon.func_174791_d().field_72449_c;
      double angleDiff = this.findAngle(diffX, diffZ);
      double angleCurrent = this.findAngle(this.pokemon.field_70159_w, this.pokemon.field_70179_y);
      double theta = angleDiff - angleCurrent;
      if (angleDiff - angleCurrent > 180.0) {
         theta = -1.0 * (360.0 - theta);
      }

      if (Math.abs(theta) > this.flyingParameters.rotationSpeed) {
         theta = this.flyingParameters.rotationSpeed;
      }

      double motionMagnitude = Math.sqrt(Math.pow(this.pokemon.field_70159_w, 2.0) + Math.pow(this.pokemon.field_70179_y, 2.0));
      double diffMagnitude = Math.sqrt(Math.pow(diffX, 2.0) + Math.pow(diffZ, 2.0));
      if (this.pokemon.field_70159_w == 0.0 && this.pokemon.field_70179_y == 0.0) {
         this.pokemon.field_70159_w = this.pokemon.field_70179_y = -0.01;
      }

      motionMagnitude += this.flyingParameters.acceleration;
      if (motionMagnitude > this.flyingParameters.maxFlySpeed) {
         motionMagnitude = this.flyingParameters.maxFlySpeed;
      }

      double desiredMotionX = Math.cos(theta) * this.pokemon.field_70159_w - Math.sin(theta) * this.pokemon.field_70179_y;
      double desiredMotionZ = Math.sin(theta) * this.pokemon.field_70159_w + Math.cos(theta) * this.pokemon.field_70179_y;
      double newMotionSpeed = Math.sqrt(Math.pow(desiredMotionX, 2.0) + Math.pow(desiredMotionZ, 2.0));
      double ratio = newMotionSpeed / motionMagnitude;
      desiredMotionX /= ratio;
      desiredMotionZ /= ratio;
      double yDist = (double)this.target.func_177956_o() - this.pokemon.func_174791_d().field_72448_b;
      this.pokemon.field_70159_w = desiredMotionX;
      this.pokemon.field_70181_x = yDist / diffMagnitude;
      this.pokemon.field_70179_y = desiredMotionZ;
      float yaw = (float)Math.atan2(-this.pokemon.field_70159_w, this.pokemon.field_70179_y);
      this.pokemon.field_70177_z = (float)((double)(yaw * 180.0F) / Math.PI);
   }

   public boolean reachedTarget() {
      if (this.target != null) {
         double dist = Math.sqrt(Math.pow(this.pokemon.func_174791_d().field_72450_a - (double)this.target.func_177958_n(), 2.0) + Math.pow(this.pokemon.func_174791_d().field_72449_c - (double)this.target.func_177952_p(), 2.0));
         if (dist <= 3.0) {
            return true;
         }
      }

      return false;
   }

   public boolean findAerialTarget() {
      double baseDir = (double)this.pokemon.field_70177_z + (this.pokemon.func_70681_au().nextDouble() - 0.5) * 60.0;

      for(int i = 0; i < 3; ++i) {
         int radius = this.pokemon.func_70681_au().nextInt(30) + 50;
         double direction = baseDir + (double)(i * 60);
         int x = (int)((double)radius * Math.cos(direction * Math.PI / 180.0));
         int z = (int)((double)radius * Math.sin(direction * Math.PI / 180.0));
         this.reusablePos.func_189533_g(this.pokemon.func_130014_f_().func_175672_r(this.reusablePos.func_181079_c(x, 1, z)));
         if (this.reusablePos.func_177956_o() > 0 && this.reusablePos.func_177956_o() <= 230) {
            this.target = this.reusablePos.func_177982_a(0, 20, 0).func_185334_h();
            return true;
         }
      }

      return false;
   }

   public BlockPos findLanding() {
      this.reusablePos.func_189533_g(this.pokemon.func_130014_f_().func_175672_r(this.pokemon.func_180425_c()));
      if (this.reusablePos.func_177956_o() > 0) {
         IBlockState blockState = this.pokemon.func_130014_f_().func_180495_p(this.reusablePos.func_177973_b(new Vec3i(0, 1, 0)));
         if (blockState.func_185917_h() && blockState.func_185913_b()) {
            return this.reusablePos.func_185334_h();
         }
      }

      return null;
   }
}

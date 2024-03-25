package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AIFlyingPersistent extends EntityAIBase {
   EntityPixelmon pixelmon;
   int ticksToChangeDirection = 0;
   int ticksToChangeSpeed = 0;
   float movespeed = 1.0F;
   int directionChange = 1000;
   int speedChange = 500;
   boolean lastChangeDirection;

   public AIFlyingPersistent(EntityPixelmon entity) {
      if (entity.getFlyingParameters() != null) {
         this.movespeed = 1.0F * entity.getFlyingParameters().flySpeedModifier;
         this.directionChange = entity.getFlyingParameters().flyRefreshRateXZ;
         this.speedChange = entity.getFlyingParameters().flyRefreshRateSpeed;
      }

      this.pixelmon = entity;
   }

   public boolean func_75250_a() {
      return true;
   }

   public boolean func_75253_b() {
      EntityPixelmon var10000 = this.pixelmon;
      var10000.field_70761_aq += (-((float)Math.atan2(this.pixelmon.field_70159_w, this.pixelmon.field_70179_y)) * 180.0F / 3.1415927F - this.pixelmon.field_70761_aq) * 0.1F;
      return true;
   }

   public void func_75246_d() {
      if (this.pixelmon.battleController != null) {
         super.func_75246_d();
      } else {
         --this.ticksToChangeDirection;
         --this.ticksToChangeSpeed;
         boolean useLastChangeDirection = false;
         World var10000 = this.pixelmon.field_70170_p;
         Vec3d var10001 = new Vec3d(this.pixelmon.field_70165_t, this.pixelmon.func_174813_aQ().field_72338_b, this.pixelmon.field_70161_v);
         double var10004 = this.pixelmon.field_70165_t + this.pixelmon.field_70159_w * 100.0;
         double var10006 = this.pixelmon.field_70161_v + this.pixelmon.field_70179_y * 100.0;
         RayTraceResult mop = var10000.func_72933_a(var10001, new Vec3d(var10004, this.pixelmon.func_174813_aQ().field_72338_b, var10006));
         if (mop == null) {
            var10000 = this.pixelmon.field_70170_p;
            var10001 = new Vec3d(this.pixelmon.field_70165_t, this.pixelmon.func_174813_aQ().field_72337_e, this.pixelmon.field_70161_v);
            var10004 = this.pixelmon.field_70165_t + this.pixelmon.field_70159_w * 100.0;
            var10006 = this.pixelmon.field_70161_v + this.pixelmon.field_70179_y * 100.0;
            mop = var10000.func_72933_a(var10001, new Vec3d(var10004, this.pixelmon.func_174813_aQ().field_72337_e, var10006));
         }

         if (mop != null) {
            useLastChangeDirection = true;
            this.ticksToChangeDirection = 0;
         }

         if (this.ticksToChangeDirection <= 0) {
            this.pickDirection(useLastChangeDirection);
            this.ticksToChangeDirection = 25 + this.pixelmon.func_70681_au().nextInt(this.directionChange);
         }

         if (this.ticksToChangeSpeed <= 0) {
            this.pickSpeed();
            this.ticksToChangeSpeed = 50 + this.pixelmon.func_70681_au().nextInt(this.speedChange);
         }

         this.pixelmon.func_191986_a(0.0F, 0.0F, this.movespeed);
         super.func_75246_d();
      }
   }

   public void pickDirection(boolean useLastChangeDirection) {
      double rotAmt;
      if (useLastChangeDirection) {
         rotAmt = (double)(this.pixelmon.func_70681_au().nextInt(5) + 5);
         if (this.lastChangeDirection) {
            rotAmt *= -1.0;
         }

         this.pixelmon.func_70638_az();
      } else {
         rotAmt = (double)(this.pixelmon.func_70681_au().nextInt(40) - 20);
         this.lastChangeDirection = rotAmt > 0.0;
      }

      EntityPixelmon var10000 = this.pixelmon;
      var10000.field_70177_z = (float)((double)var10000.field_70177_z + rotAmt);
   }

   public void pickSpeed() {
      this.movespeed = this.pixelmon.func_70681_au().nextFloat() * 0.7F + 0.5F;
   }
}

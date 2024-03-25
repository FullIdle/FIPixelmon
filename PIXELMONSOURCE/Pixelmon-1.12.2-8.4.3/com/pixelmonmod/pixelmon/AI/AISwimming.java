package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.WorldHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.Random;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AISwimming extends EntityAIBase {
   private EntityPixelmon pixelmon;
   private float swimSpeed = 1.5F;
   private float decayRate = 0.99F;
   private int depthRangeStart = 0;
   private int depthRangeEnd = 100;
   float moveSpeed;
   Random rand;
   int ticksToRefresh;
   boolean lastChangeDirection;
   boolean useLastChangeDirection;

   public AISwimming(EntityPixelmon entity) {
      if (entity.getSwimmingParameters() != null) {
         this.swimSpeed = entity.getSwimmingParameters().swimSpeed;
         this.decayRate = entity.getSwimmingParameters().decayRate;
         this.depthRangeStart = entity.getSwimmingParameters().depthRangeStart;
         this.depthRangeEnd = entity.getSwimmingParameters().depthRangeEnd;
         this.ticksToRefresh = 0;
      }

      this.pixelmon = entity;
      this.rand = entity.func_70681_au();
   }

   public boolean func_75250_a() {
      return this.depthRangeStart != -1;
   }

   public void func_75246_d() {
      if (this.pixelmon.getPokemonName().equals("Magikarp")) {
         this.swimSpeed = 0.7F;
      }

      if (this.pixelmon.battleController != null) {
         super.func_75246_d();
      } else if (this.pixelmon.func_70090_H()) {
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
         }

         --this.ticksToRefresh;
         if (this.moveSpeed == 0.0F || useLastChangeDirection || this.pixelmon.field_70159_w * this.pixelmon.field_70159_w + this.pixelmon.field_70179_y * this.pixelmon.field_70179_y < (double)(this.moveSpeed / 4.0F)) {
            this.pickDirection(useLastChangeDirection);
            this.pickSpeed();
            this.pixelmon.func_191986_a(0.0F, 0.0F, this.moveSpeed);
         }

         super.func_75246_d();
      }
   }

   public boolean func_75253_b() {
      this.moveSpeed *= this.decayRate;
      EntityPixelmon var10000 = this.pixelmon;
      var10000.field_70159_w *= (double)this.decayRate;
      var10000 = this.pixelmon;
      var10000.field_70181_x *= (double)this.decayRate;
      var10000 = this.pixelmon;
      var10000.field_70179_y *= (double)this.decayRate;
      this.pixelmon.field_70761_aq = this.pixelmon.field_70177_z;
      return true;
   }

   public void pickDirection(boolean useLastChangeDirection) {
      double rotAmt;
      if (useLastChangeDirection) {
         rotAmt = (double)(this.pixelmon.func_70681_au().nextInt(5) + 5);
         if (this.lastChangeDirection) {
            rotAmt *= -1.0;
         }
      } else {
         rotAmt = (double)(this.pixelmon.func_70681_au().nextInt(10) - 5);
         this.lastChangeDirection = rotAmt > 0.0;
      }

      EntityPixelmon var10000 = this.pixelmon;
      var10000.field_70177_z = (float)((double)var10000.field_70177_z + rotAmt);
   }

   public void pickSpeed() {
      this.moveSpeed = 2.8F * (this.pixelmon.func_70681_au().nextFloat() * this.swimSpeed + this.swimSpeed / 2.0F);
      int wdepth = WorldHelper.getWaterDepth(this.pixelmon.func_180425_c(), this.pixelmon.field_70170_p);
      if (wdepth >= this.depthRangeEnd) {
         this.pixelmon.field_70181_x = (double)((0.02F + this.rand.nextFloat()) * 0.1F);
      } else if (wdepth <= this.depthRangeStart) {
         this.pixelmon.field_70181_x = (double)((-0.02F - this.rand.nextFloat()) * 0.1F);
      } else {
         this.pixelmon.field_70181_x = (double)((this.rand.nextFloat() - 0.5F) * 0.1F);
      }

   }
}

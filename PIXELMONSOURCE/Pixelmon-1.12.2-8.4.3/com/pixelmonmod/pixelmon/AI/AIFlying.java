package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AIFlying extends EntityAIBase {
   private static final long OWNER_FIND_INTERVAL = 5000L;
   private static final double OWNER_DISTANCE_TO_TAKEOFF = 100.0;
   private final EntityPixelmon pixelmon;
   private long nextOwnerCheckTime;
   private BlockPos currentFlightTarget;
   private Random rand;
   private boolean takingOff = false;
   private int nextWingBeat = 10;
   private int wingBeatTick = 0;
   boolean lastChangeDirection;
   int flightTicks = 0;
   double takeOffSpeed = 0.0;
   int targetHeight = 0;

   public AIFlying(EntityPixelmon entity) {
      this.pixelmon = entity;
      this.rand = entity.func_70681_au();
      this.nextOwnerCheckTime = System.currentTimeMillis();
      this.func_75248_a(1);
   }

   public boolean func_75250_a() {
      return this.pixelmon.field_70122_E && this.pixelmon.func_70902_q() == null && this.pixelmon.getFlyingParameters() != null && this.checkTakeOffConditions();
   }

   public boolean func_75253_b() {
      return !this.pixelmon.field_70122_E;
   }

   public void func_75249_e() {
      this.takeOff();
   }

   public void func_75251_c() {
      super.func_75251_c();
   }

   public void func_75246_d() {
      ++this.flightTicks;
      if (this.flightTicks > 30 && this.takingOff || this.takingOff && this.pixelmon.field_70163_u >= (double)this.targetHeight) {
         this.takingOff = false;
         this.flightTicks = 0;
      }

      if (this.takingOff) {
         this.pixelmon.func_191986_a((float)this.pixelmon.getPokemonData().getStat(StatsType.Speed) / 500.0F, 0.0F, 0.0F);
         this.pixelmon.field_70181_x = this.takeOffSpeed;
      }

      if (this.pixelmon.func_70902_q() == null) {
         this.lookForOwnerEntity();
      }

      this.checkForLandingSpot();
      AxisAlignedBB box = this.pixelmon.func_174813_aQ();
      RayTraceResult mop = this.pixelmon.field_70170_p.func_72933_a(new Vec3d(this.pixelmon.field_70165_t, box.field_72338_b, this.pixelmon.field_70161_v), new Vec3d(this.pixelmon.field_70165_t + this.pixelmon.field_70159_w * 100.0, box.field_72338_b, this.pixelmon.field_70161_v + this.pixelmon.field_70179_y * 100.0));
      if (mop == null) {
         mop = this.pixelmon.field_70170_p.func_72933_a(new Vec3d(this.pixelmon.field_70165_t, box.field_72337_e, this.pixelmon.field_70161_v), new Vec3d(this.pixelmon.field_70165_t + this.pixelmon.field_70159_w * 100.0, box.field_72337_e, this.pixelmon.field_70161_v + this.pixelmon.field_70179_y * 100.0));
      }

      if (this.hasLandingSpot()) {
         if (mop == null) {
            double d0 = (double)this.currentFlightTarget.func_177958_n() + 0.5 - this.pixelmon.field_70165_t;
            double d1 = (double)this.currentFlightTarget.func_177956_o() + 0.1 - this.pixelmon.field_70163_u;
            double d2 = (double)this.currentFlightTarget.func_177952_p() + 0.5 - this.pixelmon.field_70161_v;
            EntityPixelmon var10000 = this.pixelmon;
            var10000.field_70159_w += (Math.signum(d0) * 1.0 - this.pixelmon.field_70159_w) * 0.10000000149011612;
            var10000 = this.pixelmon;
            var10000.field_70181_x += (Math.signum(d1) * 0.699999988079071 - this.pixelmon.field_70181_x) * 0.10000000149011612;
            var10000 = this.pixelmon;
            var10000.field_70179_y += (Math.signum(d2) * 1.0 - this.pixelmon.field_70179_y) * 0.10000000149011612;
            float f = (float)(Math.atan2(this.pixelmon.field_70179_y, this.pixelmon.field_70159_w) * 180.0 / Math.PI) - 90.0F;
            float f1 = MathHelper.func_76142_g(f - this.pixelmon.field_70177_z);
            this.pixelmon.func_191989_p(0.5F);
            var10000 = this.pixelmon;
            var10000.field_70177_z += f1;
         }
      } else {
         this.maintainFlight(mop != null);
      }

      super.func_75246_d();
   }

   private void checkForLandingSpot() {
      if (this.currentFlightTarget != null && (!this.pixelmon.field_70170_p.func_175623_d(this.currentFlightTarget) || this.currentFlightTarget.func_177956_o() < 1)) {
         this.currentFlightTarget = null;
      }

      if (this.currentFlightTarget == null || this.rand.nextInt(30) == 0) {
         this.currentFlightTarget = new BlockPos((int)(this.pixelmon.field_70165_t + this.pixelmon.field_70159_w * 200.0 + (double)this.rand.nextInt(10) - 5.0), 0, (int)(this.pixelmon.field_70161_v + this.pixelmon.field_70179_y * 200.0 + (double)this.rand.nextInt(10) - 5.0));
         this.currentFlightTarget = this.pixelmon.field_70170_p.func_175672_r(this.currentFlightTarget);
         IBlockState state = this.pixelmon.field_70170_p.func_180495_p(this.currentFlightTarget);
         Material m = state.func_185904_a();
         this.currentFlightTarget = this.currentFlightTarget.func_177984_a();
         if (this.pixelmon.getFlyingParameters() != null && !this.pixelmon.getFlyingParameters().willLandInMaterial(m) || !this.pixelmon.field_70170_p.func_175623_d(this.currentFlightTarget)) {
            this.currentFlightTarget = null;
         }
      }

   }

   private boolean hasLandingSpot() {
      return this.currentFlightTarget != null;
   }

   private void maintainFlight(boolean hasObstacle) {
      ++this.wingBeatTick;
      if (hasObstacle || this.wingBeatTick >= this.nextWingBeat) {
         this.pickDirection(hasObstacle);
         this.nextWingBeat = this.pixelmon.getFlyingParameters().flapRate + (int)(Math.random() * 0.4 * (double)this.pixelmon.getFlyingParameters().flapRate - 0.2 * (double)this.pixelmon.getFlyingParameters().flapRate);
         this.pixelmon.func_191986_a(0.0F, 0.0F, 4.0F + (float)this.pixelmon.getPokemonData().getStat(StatsType.Speed) / 100.0F * this.pixelmon.getFlyingParameters().flySpeedModifier);
         this.pixelmon.field_70181_x = (double)(this.pixelmon.getFlyingParameters().flapRate + 1) * 0.01;
         this.wingBeatTick = 0;
      }

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

   private void lookForOwnerEntity() {
      if (this.pixelmon.func_70902_q() != null && System.currentTimeMillis() > this.nextOwnerCheckTime) {
         this.nextOwnerCheckTime = System.currentTimeMillis() + 5000L;
         this.currentFlightTarget = new BlockPos((int)this.pixelmon.func_70902_q().field_70165_t, (int)this.pixelmon.func_70902_q().field_70163_u + 1, (int)this.pixelmon.func_70902_q().field_70161_v);
      }

   }

   private boolean checkTakeOffConditions() {
      if (this.pixelmon.grounded) {
         return false;
      } else if (this.pixelmon.func_70902_q() != null && this.pixelmon.func_70902_q().func_70089_S() && this.pixelmon.func_70068_e(this.pixelmon.func_70902_q()) > 100.0) {
         return true;
      } else {
         EntityPlayer nearest = this.pixelmon.field_70170_p.func_72890_a(this.pixelmon, 6.0);
         return nearest != null && nearest != this.pixelmon.func_70902_q() || Math.random() < 0.015;
      }
   }

   private void takeOff() {
      this.pixelmon.isFlying = true;
      this.takingOff = true;
      this.flightTicks = 0;
      this.targetHeight = (int)this.pixelmon.field_70163_u + (int)(Math.random() * (double)(this.pixelmon.getFlyingParameters().flyHeightMax - this.pixelmon.getFlyingParameters().flyHeightMin)) + this.pixelmon.getFlyingParameters().flyHeightMin;
      this.pixelmon.func_70107_b(this.pixelmon.field_70165_t, this.pixelmon.field_70163_u - 1.0, this.pixelmon.field_70161_v);
      this.pixelmon.field_70170_p.func_184148_a((EntityPlayer)null, this.pixelmon.field_70165_t, this.pixelmon.field_70163_u, this.pixelmon.field_70161_v, SoundEvents.field_187744_z, SoundCategory.NEUTRAL, 0.8F, 1.0F);
      this.takeOffSpeed = 0.22 + (double)((float)this.pixelmon.getPokemonData().getStat(StatsType.Speed) / 300.0F);
      this.pixelmon.func_70091_d(MoverType.SELF, this.pixelmon.func_70681_au().nextDouble() - 0.5, this.takeOffSpeed, this.pixelmon.func_70681_au().nextDouble() - 0.5);
   }
}

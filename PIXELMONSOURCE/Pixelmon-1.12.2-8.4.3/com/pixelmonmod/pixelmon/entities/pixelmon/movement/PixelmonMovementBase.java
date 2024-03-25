package com.pixelmonmod.pixelmon.entities.pixelmon.movement;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6Moves;
import java.util.List;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class PixelmonMovementBase {
   protected Entity6Moves pixelmon;

   public PixelmonMovementBase(Entity6Moves dragon) {
      this.pixelmon = dragon;
   }

   public abstract void handleMovement(List var1);

   public abstract void onLivingUpdate();

   public abstract void updatePassenger(Entity var1, Vector3f var2);

   protected Matrix3d createRotationMatrix(Vec3d lookVec) {
      Matrix3d matOut = new Matrix3d();
      matOut.setIdentity();
      Matrix3d matW = new Matrix3d();
      matW.m01 = -lookVec.field_72449_c;
      matW.m02 = lookVec.field_72448_b;
      matW.m10 = lookVec.field_72449_c;
      matW.m12 = -lookVec.field_72450_a;
      matW.m20 = -lookVec.field_72448_b;
      matW.m21 = lookVec.field_72450_a;
      Matrix3d matW2 = (Matrix3d)matW.clone();
      double angle = (double)(-this.pixelmon.rotationRoll * 3.1415927F / 180.0F);
      matW.mul(Math.sin(angle));
      matW2.mul(2.0 * Math.sin(angle / 2.0) * Math.sin(angle / 2.0));
      matW2.mul(matW2);
      matOut.add(matW);
      matOut.add(matW2);
      return matOut;
   }

   public void travelCustom(float strafe, float forward) {
      if (this.pixelmon.lastSpeed == -1.0F) {
         this.pixelmon.lastSpeed = forward;
      }

      if (this.pixelmon.lastStrafe == -1.0F) {
         this.pixelmon.lastStrafe = strafe;
      }

      if (this.pixelmon.lastStrafeUp == -1.0F) {
         this.pixelmon.lastStrafeUp = this.pixelmon.strafeUpDown;
      }

      if (this.pixelmon.getIsHovering()) {
         this.pixelmon.field_70125_A += (this.pixelmon.hoverRotationPitch - this.pixelmon.field_70125_A) * this.pixelmon.getBaseStats().mountedFlying.pitchRate / 8.0F;
      }

      EntityLivingBase passenger = (EntityLivingBase)this.pixelmon.func_184179_bs();
      float pitch = this.pixelmon.field_70125_A;
      float f1 = MathHelper.func_76126_a(-1.0F * this.pixelmon.field_70177_z * 0.017453292F);
      float f2 = MathHelper.func_76134_b(-1.0F * this.pixelmon.field_70177_z * 0.017453292F);
      float f3 = MathHelper.func_76126_a(pitch * 0.017453292F);
      float f4 = MathHelper.func_76134_b(pitch * 0.017453292F);
      Vector3f movementVector = new Vector3f(f4 * f1, f3, f4 * f2);
      Vector3f strafeVector = new Vector3f();
      strafeVector.cross(movementVector, new Vector3f(0.0F, 1.0F, 0.0F));
      Vector3f strafeUpVector = new Vector3f();
      strafeUpVector.cross(movementVector, strafeVector);
      float speed;
      if (forward > this.pixelmon.lastSpeed) {
         speed = this.pixelmon.lastSpeed * (1.0F - this.pixelmon.getBaseStats().mountedFlying.accelerationRate) + this.pixelmon.getBaseStats().mountedFlying.accelerationRate * forward;
      } else if (!this.pixelmon.getIsHovering()) {
         speed = this.pixelmon.lastSpeed * (1.0F - this.pixelmon.getBaseStats().mountedFlying.decelerationRate) + this.pixelmon.getBaseStats().mountedFlying.decelerationRate * forward;
      } else {
         speed = this.pixelmon.lastSpeed * (1.0F - this.pixelmon.getBaseStats().mountedFlying.hoverDecelerationRate) + this.pixelmon.getBaseStats().mountedFlying.hoverDecelerationRate * forward;
      }

      if (strafe != this.pixelmon.lastStrafe) {
         strafe = this.pixelmon.lastStrafe * this.pixelmon.getBaseStats().mountedFlying.strafeAccelerationRate + (1.0F - this.pixelmon.getBaseStats().mountedFlying.strafeAccelerationRate) * strafe;
      }

      if (this.pixelmon.strafeUpDown != this.pixelmon.lastStrafeUp) {
         this.pixelmon.strafeUpDown = this.pixelmon.lastStrafeUp * this.pixelmon.getBaseStats().mountedFlying.strafeAccelerationRate + (1.0F - this.pixelmon.getBaseStats().mountedFlying.strafeAccelerationRate) * this.pixelmon.strafeUpDown;
      }

      if (this.pixelmon.field_70170_p.field_72995_K) {
         this.calculateRoll(strafe);
      }

      strafeVector.scale(-1.0F * strafe);
      if (strafeUpVector.y > 0.0F) {
         strafeUpVector.scale(this.pixelmon.strafeUpDown);
      } else {
         strafeUpVector.scale(-1.0F * this.pixelmon.strafeUpDown);
      }

      movementVector.scale(speed);
      this.pixelmon.lastSpeed = speed;
      this.pixelmon.lastStrafe = strafe;
      double takeOffPower = 0.0;
      if (this.pixelmon.takeOffTicks > 0) {
         takeOffPower = 1.0;
         if (this.pixelmon.takeOffTicks <= 10) {
            takeOffPower = (double)this.pixelmon.takeOffTicks / 10.0;
         }
      }

      this.pixelmon.field_70159_w = (double)(movementVector.x + strafeVector.x + strafeUpVector.x);
      this.pixelmon.field_70181_x = (double)(movementVector.y + strafeVector.y + strafeUpVector.y) + takeOffPower / 3.0;
      this.pixelmon.field_70179_y = (double)(movementVector.z + strafeVector.z + strafeUpVector.z);
      if (passenger != null && !this.pixelmon.getIsHovering()) {
         Entity6Moves var10000 = this.pixelmon;
         var10000.field_70181_x += (double)this.pixelmon.thermalPower / 4.0 - (double)this.pixelmon.fallRate;
      }

      this.pixelmon.func_70091_d(MoverType.SELF, this.pixelmon.field_70159_w, this.pixelmon.field_70181_x, this.pixelmon.field_70179_y);
   }

   private void calculateRoll(float strafe) {
      float strafeRotationRoll = strafe / 0.5F * this.pixelmon.getBaseStats().mountedFlying.strafeRollConversion;
      float turnRotationRoll = -1.0F * (this.pixelmon.field_70177_z - this.pixelmon.field_70126_B) / 8.0F * this.pixelmon.getBaseStats().mountedFlying.strafeRollConversion;
      float degreesPast360 = 360.0F - this.pixelmon.field_70177_z + this.pixelmon.field_70126_B;
      float degreesBack360 = this.pixelmon.field_70177_z + 360.0F - this.pixelmon.field_70126_B;
      float diff = Math.abs(this.pixelmon.field_70177_z - this.pixelmon.field_70126_B);
      if (degreesPast360 < diff && degreesPast360 < degreesBack360) {
         turnRotationRoll = -1.0F * degreesPast360 / 8.0F * this.pixelmon.getBaseStats().mountedFlying.strafeRollConversion;
      } else if (degreesBack360 < diff && degreesBack360 < degreesPast360) {
         turnRotationRoll = -1.0F * degreesBack360 / 8.0F * this.pixelmon.getBaseStats().mountedFlying.strafeRollConversion;
      }

      this.pixelmon.prevRotationRoll = this.pixelmon.rotationRoll;
      if (Math.abs(strafeRotationRoll) > Math.abs(turnRotationRoll)) {
         this.pixelmon.rotationRoll = 0.8F * this.pixelmon.prevRotationRoll + 0.2F * strafeRotationRoll;
      } else {
         this.pixelmon.rotationRoll = 0.8F * this.pixelmon.prevRotationRoll + 0.2F * turnRotationRoll;
         if (this.pixelmon.rotationRoll > 45.0F) {
            this.pixelmon.rotationRoll = 45.0F;
         }

         if (this.pixelmon.rotationRoll < -45.0F) {
            this.pixelmon.rotationRoll = -45.0F;
         }
      }

   }
}

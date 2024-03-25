package com.pixelmonmod.pixelmon.entities.pixelmon.movement;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6Moves;
import com.pixelmonmod.pixelmon.enums.EnumKeybinds;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class PixelmonMovementFlying extends PixelmonMovementBase {
   public PixelmonMovementFlying(Entity6Moves dragon) {
      super(dragon);
   }

   public void handleMovement(List movements) {
      Iterator var2 = movements.iterator();

      while(var2.hasNext()) {
         EnumKeybinds movement = (EnumKeybinds)var2.next();
         if (movement == EnumKeybinds.Left) {
            this.pixelmon.field_70702_br = 0.5F;
         }

         if (movement == EnumKeybinds.Right) {
            this.pixelmon.field_70702_br = -0.5F;
         }

         Entity6Moves var10000;
         if (movement == EnumKeybinds.Forward) {
            var10000 = this.pixelmon;
            var10000.field_70125_A -= 0.5F * this.pixelmon.getBaseStats().mountedFlying.pitchRate;
            if (this.pixelmon.field_70125_A < this.pixelmon.getBaseStats().mountedFlying.lowerAngleLimit) {
               this.pixelmon.field_70125_A = this.pixelmon.getBaseStats().mountedFlying.lowerAngleLimit;
            }
         }

         if (movement == EnumKeybinds.Back) {
            var10000 = this.pixelmon;
            var10000.field_70125_A += 0.5F * this.pixelmon.getBaseStats().mountedFlying.pitchRate;
            if (this.pixelmon.field_70125_A > this.pixelmon.getBaseStats().mountedFlying.upperAngleLimit) {
               this.pixelmon.field_70125_A = this.pixelmon.getBaseStats().mountedFlying.upperAngleLimit;
            }
         }

         Entity6Moves var10001;
         if (movement == EnumKeybinds.Jump) {
            if (this.pixelmon.getIsHovering()) {
               this.pixelmon.strafeUpDown = 1.0F;
            } else {
               var10000 = this.pixelmon;
               var10000.moveMultiplier += 0.005F;
               var10001 = this.pixelmon;
               if (this.pixelmon.moveMultiplier > Entity6Moves.maxMoveMultiplier) {
                  var10001 = this.pixelmon;
                  this.pixelmon.moveMultiplier = Entity6Moves.maxMoveMultiplier;
               }
            }
         }

         if (movement == EnumKeybinds.Sprint) {
            if (this.pixelmon.getIsHovering()) {
               --this.pixelmon.strafeUpDown;
            } else {
               var10000 = this.pixelmon;
               var10000.moveMultiplier -= 0.005F;
               var10001 = this.pixelmon;
               if (this.pixelmon.moveMultiplier < Entity6Moves.minMoveMultiplier) {
                  var10001 = this.pixelmon;
                  this.pixelmon.moveMultiplier = Entity6Moves.minMoveMultiplier;
               }
            }
         }
      }

      boolean breakHover = false;
      Iterator var6 = movements.iterator();

      while(var6.hasNext()) {
         EnumKeybinds movement = (EnumKeybinds)var6.next();
         if (movement.breaksHover() && this.pixelmon.getIsHovering()) {
            breakHover = true;
         }
      }

      if (breakHover) {
         this.pixelmon.toggleHover();
      }

   }

   public void onLivingUpdate() {
      if (!this.pixelmon.field_70170_p.field_72995_K && this.pixelmon.takeOffTicks == 0 && this.pixelmon.field_70170_p.func_184143_b(this.pixelmon.func_174813_aQ().func_72321_a(0.2, 0.2, 0.2).func_72321_a(-0.2, -0.2, -0.2))) {
         this.pixelmon.setIsFlying(false);
         this.pixelmon.field_70125_A = 0.0F;
         this.pixelmon.rotationRoll = 0.0F;
         this.pixelmon.onLanding();
      }

      this.pixelmon.thermalPower = Thermals.getThermalPower(this.pixelmon.field_70170_p, this.pixelmon.func_180425_c());
      if (this.pixelmon.takeOffTicks > 0) {
         --this.pixelmon.takeOffTicks;
      }

      Entity6Moves var10000;
      if (this.pixelmon.func_184179_bs() != null) {
         float playerYaw = this.pixelmon.getFlyingDirection();
         Entity var7;
         if (playerYaw > 360.0F) {
            playerYaw -= 360.0F;
            var7 = this.pixelmon.func_184179_bs();
            var7.field_70177_z -= 360.0F;
         }

         if (playerYaw < 0.0F) {
            playerYaw += 360.0F;
            var7 = this.pixelmon.func_184179_bs();
            var7.field_70177_z += 360.0F;
         }

         float dragonYaw = this.pixelmon.field_70177_z;
         if (dragonYaw > 360.0F) {
            dragonYaw -= 360.0F;
            var10000 = this.pixelmon;
            var10000.field_70177_z -= 360.0F;
         }

         if (dragonYaw < 0.0F) {
            dragonYaw += 360.0F;
            var10000 = this.pixelmon;
            var10000.field_70177_z += 360.0F;
         }

         float degreesPast360 = 360.0F - dragonYaw + playerYaw;
         float degreesBack360 = dragonYaw + 360.0F - playerYaw;
         float diff = Math.abs(playerYaw - dragonYaw);
         float degreeShift = 13.0F * this.pixelmon.getBaseStats().mountedFlying.turnRate * 1.0F / (1.0F + this.pixelmon.field_191988_bg);
         if (dragonYaw != playerYaw) {
            if (!(diff <= degreeShift) && !(degreesPast360 <= degreeShift) && !(degreesBack360 <= degreeShift)) {
               if (degreesPast360 < diff && degreesPast360 < degreesBack360) {
                  var10000 = this.pixelmon;
                  var10000.field_70177_z += degreeShift;
               } else if (degreesBack360 < diff && degreesBack360 < degreesPast360) {
                  var10000 = this.pixelmon;
                  var10000.field_70177_z -= degreeShift;
               } else if (dragonYaw < playerYaw) {
                  var10000 = this.pixelmon;
                  var10000.field_70177_z += degreeShift;
               } else {
                  var10000 = this.pixelmon;
                  var10000.field_70177_z -= degreeShift;
               }
            } else {
               this.pixelmon.field_70177_z = playerYaw;
            }
         }
      }

      this.pixelmon.field_70701_bs = -0.02F;
      this.pixelmon.calcMoveSpeed();
      this.pixelmon.hoverRotationPitch = 20.0F;
      this.travelCustom(this.pixelmon.field_70702_br, this.pixelmon.field_191988_bg);
      var10000 = this.pixelmon;
      var10000.field_70702_br *= 0.94F;
      var10000 = this.pixelmon;
      var10000.strafeUpDown *= 0.94F;
   }

   public void updatePassenger(Entity passenger, Vector3f offsets) {
      Vec3d lookVec = new Vec3d(this.pixelmon.field_70159_w, this.pixelmon.field_70181_x, this.pixelmon.field_70179_y);
      if (this.pixelmon.getIsHovering()) {
         lookVec = this.pixelmon.func_70040_Z();
         lookVec = lookVec.func_178787_e(new Vec3d(0.0, -2.0 * lookVec.field_72448_b, 0.0));
      }

      if (this.pixelmon.getStaysHorizontalInRender()) {
         lookVec = new Vec3d(this.pixelmon.field_70159_w, 0.0, this.pixelmon.field_70179_y);
      }

      lookVec = lookVec.func_72432_b();
      new Vec3d(0.0, 1.0, 0.0);
      Vec3d vec = new Vec3d(0.0, 0.0, 0.0);
      Vec3d yVec = null;
      if (this.pixelmon.getStaysHorizontalInRender()) {
         yVec = lookVec.func_72431_c(new Vec3d(1.0, 0.0, 1.0));
      } else {
         Vec3d horizVec = lookVec.func_72431_c(new Vec3d(0.0, 1.0, 0.0));
         yVec = lookVec.func_72431_c(horizVec);
      }

      if (yVec.field_72448_b < 0.0) {
         yVec = yVec.func_186678_a(-1.0);
      }

      yVec = yVec.func_72432_b();
      double yPosScale;
      if (offsets.z != 0.0F) {
         yPosScale = (double)((offsets.z + this.pixelmon.field_70131_O) * this.pixelmon.getPixelmonScale() * this.pixelmon.getScaleFactor());
         vec = vec.func_72441_c(yVec.field_72450_a * yPosScale, yVec.field_72448_b * yPosScale, yVec.field_72449_c * yPosScale);
      }

      if (offsets.y != 0.0F) {
         yPosScale = (double)(offsets.y * this.pixelmon.getPixelmonScale() * this.pixelmon.getScaleFactor());
         vec = vec.func_72441_c(lookVec.field_72450_a * yPosScale, lookVec.field_72448_b * yPosScale, lookVec.field_72449_c * yPosScale);
      }

      Matrix3d matRot = this.createRotationMatrix(lookVec);
      Vector3d vecRot = new Vector3d(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
      matRot.transform(vecRot);
      passenger.func_70107_b(this.pixelmon.field_70165_t + vecRot.x, this.pixelmon.field_70163_u + vecRot.y, this.pixelmon.field_70161_v + vecRot.z);
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.movement;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6Moves;
import com.pixelmonmod.pixelmon.enums.EnumKeybinds;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class PixelmonMovementGround extends PixelmonMovementBase {
   public PixelmonMovementGround(Entity6Moves dragon) {
      super(dragon);
   }

   public void handleMovement(List movements) {
      Iterator var2 = movements.iterator();

      while(true) {
         while(true) {
            EnumKeybinds movement;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               movement = (EnumKeybinds)var2.next();
            } while(movement != EnumKeybinds.Jump);

            if (this.pixelmon.field_70773_bE > 0 && this.pixelmon.getCanFly()) {
               this.pixelmon.takeOff();
            } else if (this.pixelmon.field_70773_bE <= 0 && this.pixelmon.field_70122_E) {
               this.pixelmon.doJump();
               this.pixelmon.field_70773_bE = 20;
            }
         }
      }
   }

   public void onLivingUpdate() {
      this.pixelmon.field_70125_A = 0.0F;
      this.pixelmon.rotationRoll = 0.0F;
      if (this.pixelmon.field_70773_bE > 0) {
         --this.pixelmon.field_70773_bE;
      }

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
         Entity6Moves var10000;
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
         float degreeShift = 13.0F / (1.0F + this.pixelmon.field_191988_bg);
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

   }

   public void updatePassenger(Entity passenger, Vector3f offsets) {
      Vec3d lV = this.pixelmon.func_70040_Z();
      Vec3d lookVec = new Vec3d(lV.field_72450_a, 0.0, lV.field_72449_c);
      lookVec = lookVec.func_72432_b();
      new Vec3d(0.0, 1.0, 0.0);
      Vec3d vec = new Vec3d(0.0, 0.0, 0.0);
      Vec3d horizVec = lookVec.func_72431_c(new Vec3d(0.0, 1.0, 0.0));
      Vec3d yVec = lookVec.func_72431_c(horizVec);
      if (yVec.field_72448_b < 0.0) {
         yVec = yVec.func_186678_a(-1.0);
      }

      yVec = yVec.func_72432_b();
      Vector3d vecRot = new Vector3d(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
      passenger.func_70107_b(this.pixelmon.field_70165_t + vecRot.x, this.pixelmon.field_70163_u + vecRot.y, this.pixelmon.field_70161_v + vecRot.z);
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.movement;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity6Moves;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntityMoveHelper.Action;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class PixelmonMoveHelper extends EntityMoveHelper {
   Entity6Moves pixelmon;
   public FlyMode mode;
   int courseChangeCooldown;
   boolean forceUpdate;

   public PixelmonMoveHelper(EntityLiving entitylivingIn) {
      super(entitylivingIn);
      this.mode = FlyMode.Fly;
      this.courseChangeCooldown = 0;
      this.forceUpdate = false;
      this.pixelmon = (Entity6Moves)entitylivingIn;
   }

   public void func_75641_c() {
      super.func_75641_c();
   }

   private boolean isNotColliding(double x, double y, double z, double distance) {
      double d0 = (x - this.pixelmon.field_70165_t) / distance;
      double d1 = (y - this.pixelmon.field_70163_u) / distance;
      double d2 = (z - this.pixelmon.field_70161_v) / distance;
      AxisAlignedBB axisalignedbb = this.pixelmon.func_174813_aQ();

      for(int i = 1; (double)i < distance; ++i) {
         axisalignedbb = axisalignedbb.func_72317_d(d0, d1, d2);
         if (!this.pixelmon.field_70170_p.func_184144_a(this.pixelmon, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      Vec3d motion = (new Vec3d(this.pixelmon.field_70159_w, this.pixelmon.field_70181_x, this.pixelmon.field_70179_y)).func_72432_b().func_186678_a(distance);
      RayTraceResult mop = this.pixelmon.field_70170_p.func_72933_a(new Vec3d(this.pixelmon.field_70165_t, this.pixelmon.func_174813_aQ().field_72338_b, this.pixelmon.field_70161_v), new Vec3d(this.pixelmon.field_70165_t + motion.field_72450_a, this.pixelmon.func_174813_aQ().field_72338_b + motion.field_72448_b, this.pixelmon.field_70161_v + motion.field_72449_c));
      if (mop == null) {
         mop = this.pixelmon.field_70170_p.func_72933_a(new Vec3d(this.pixelmon.field_70165_t, this.pixelmon.func_174813_aQ().field_72337_e, this.pixelmon.field_70161_v), new Vec3d(this.pixelmon.field_70165_t + motion.field_72450_a, this.pixelmon.func_174813_aQ().field_72337_e + motion.field_72448_b, this.pixelmon.field_70161_v + motion.field_72449_c));
      }

      if (mop == null) {
         return false;
      } else {
         System.out.println("colliding");
         return true;
      }
   }

   public void setMovementMode(FlyMode mode) {
      this.forceUpdate = true;
      this.mode = mode;
   }

   public void setAttackTarget(EntityLivingBase target) {
      this.field_188491_h = Action.MOVE_TO;
      this.field_75646_b = target.field_70165_t;
      this.field_75647_c = target.field_70163_u;
      this.field_75644_d = target.field_70161_v;
   }
}

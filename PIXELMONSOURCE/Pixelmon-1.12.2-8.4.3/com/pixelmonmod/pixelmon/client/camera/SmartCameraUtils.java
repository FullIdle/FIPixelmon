package com.pixelmonmod.pixelmon.client.camera;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SmartCameraUtils {
   public static boolean canCameraSeeTargetFrom(EntityCamera camera, ICameraTarget t, double x, double y, double z) {
      return camera.field_70170_p.func_72933_a(new Vec3d(x, y, z), new Vec3d(t.getX() + t.getXSeeOffset(), t.getY() + t.getYSeeOffset(), t.getZ() + t.getZSeeOffset())) == null;
   }

   public static boolean canCameraSeeTargetFrom(EntityCamera camera, double x, double y, double z) {
      return canCameraSeeTargetFrom(camera, camera.getTarget(), x, y, z);
   }

   public static boolean canCameraSeeTargetFrom(EntityCamera camera, BlockPos pos) {
      return canCameraSeeTargetFrom(camera, camera.getTarget(), (double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p());
   }

   public static boolean canCameraSeeTarget(EntityCamera camera, ICameraTarget t) {
      return camera.field_70170_p.func_72933_a(new Vec3d(camera.field_70165_t, camera.field_70163_u, camera.field_70161_v), new Vec3d(t.getX() + t.getXSeeOffset(), t.getY() + t.getYSeeOffset(), t.getZ() + t.getZSeeOffset())) == null;
   }

   public static boolean canCameraSeeTarget(EntityCamera camera) {
      return canCameraSeeTarget(camera, camera.getTarget());
   }
}

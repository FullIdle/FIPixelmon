package com.pixelmonmod.pixelmon.client.camera;

public class CameraTargetLocation implements ICameraTarget {
   int x;
   int y;
   int z;

   public CameraTargetLocation(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public double getX() {
      return (double)this.x;
   }

   public double getXSeeOffset() {
      return 0.0;
   }

   public double getY() {
      return (double)this.y;
   }

   public double getYSeeOffset() {
      return 0.0;
   }

   public double getZ() {
      return (double)this.z;
   }

   public double getZSeeOffset() {
      return 0.0;
   }

   public double minimumCameraDistance() {
      return 0.0;
   }

   public boolean isValidTarget() {
      return true;
   }

   public boolean hasChanged() {
      return false;
   }

   public boolean setTargetData(Object o) {
      if (o instanceof int[] && ((int[])((int[])o)).length == 3) {
         this.x = ((int[])((int[])o))[0];
         this.y = ((int[])((int[])o))[1];
         this.z = ((int[])((int[])o))[2];
         return true;
      } else {
         return false;
      }
   }

   public Object getTargetData() {
      return new int[]{this.x, this.y, this.z};
   }
}

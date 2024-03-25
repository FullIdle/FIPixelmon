package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import javax.vecmath.Vector3f;

public class RidingOffsets {
   public Vector3f standing;
   public Vector3f moving;

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RidingOffsets)) {
         return false;
      } else {
         RidingOffsets other = (RidingOffsets)o;
         if (other.standing != null && this.standing != null && !other.standing.equals(this.standing)) {
            return false;
         } else if (other.moving != null && this.moving != null && !other.moving.equals(this.moving)) {
            return false;
         } else if ((other.standing == null || this.standing != null) && (other.standing != null || this.standing == null)) {
            return (other.moving == null || this.moving != null) && (other.moving != null || this.moving == null);
         } else {
            return false;
         }
      }
   }

   public void setStandingOffsets(double x, double y, double z) {
      this.standing = new Vector3f();
      this.standing.x = (float)x;
      this.standing.y = (float)y;
      this.standing.z = (float)z;
   }

   public void setMovingOffsets(double x, double y, double z) {
      this.moving = new Vector3f();
      this.moving.x = (float)x;
      this.moving.y = (float)y;
      this.moving.z = (float)z;
   }
}

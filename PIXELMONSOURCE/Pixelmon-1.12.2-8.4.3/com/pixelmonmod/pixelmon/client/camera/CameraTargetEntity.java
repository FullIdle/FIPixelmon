package com.pixelmonmod.pixelmon.client.camera;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.Entity;

public class CameraTargetEntity implements ICameraTarget {
   Entity entity;
   private double lastX = 0.0;
   private double lastY = 0.0;
   private double lastZ = 0.0;

   public CameraTargetEntity(Entity e) {
      this.entity = e;
   }

   public double getX() {
      return this.entity.field_70165_t;
   }

   public double getY() {
      return this.entity.field_70163_u;
   }

   public double getZ() {
      return this.entity.field_70161_v;
   }

   public boolean isValidTarget() {
      return this.entity != null && !this.entity.field_70128_L && (!(this.entity instanceof EntityPixelmon) || ((EntityPixelmon)this.entity).func_110143_aJ() > 0.0F);
   }

   public double getXSeeOffset() {
      return 0.0;
   }

   public double getYSeeOffset() {
      return (double)this.entity.func_70047_e();
   }

   public double getZSeeOffset() {
      return 0.0;
   }

   public double minimumCameraDistance() {
      return 0.0;
   }

   public boolean hasChanged() {
      boolean r = false;
      if (this.entity.field_70165_t != this.lastX || this.entity.field_70163_u != this.lastY || this.entity.field_70161_v != this.lastZ) {
         r = true;
      }

      this.lastX = this.entity.field_70165_t;
      this.lastY = this.entity.field_70163_u;
      this.lastZ = this.entity.field_70161_v;
      return r;
   }

   public boolean setTargetData(Object o) {
      if (o instanceof Entity) {
         this.entity = (Entity)o;
         return true;
      } else {
         return false;
      }
   }

   public Object getTargetData() {
      return this.entity;
   }
}

package com.pixelmonmod.pixelmon.client.models.animations;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import java.util.Iterator;
import net.minecraft.client.model.ModelRenderer;

public class ModuleHead extends Module {
   IModulized head;
   float headStartAngleX;
   float headStartAngleY;
   public float ymaximum;
   public float xmaximum;
   public float xoffset;
   public float yoffset;
   public int headAxis;
   public int ydirection;

   public ModuleHead(IModulized head) {
      this.head = head;
      this.headStartAngleX = this.head.getValue(EnumGeomData.xrot);
      this.headStartAngleY = this.head.getValue(EnumGeomData.yrot);
   }

   public ModuleHead(ModelRenderer head) {
      this((IModulized)(new ModulizedRenderWrapper(head)));
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float rotateAnglePitch, float rotateAngleYaw) {
      this.xrD = rotateAngleYaw * 0.017453292F;
      this.yrD = rotateAnglePitch * 0.017453292F;
      this.zrD = rotateAnglePitch * 0.017453292F;
      float xrotation = -this.xoffset + this.xrD + this.headStartAngleX;
      if (this.xmaximum != 0.0F) {
         if (xrotation > this.xmaximum * 0.017453292F) {
            xrotation = this.xmaximum * 0.017453292F;
         }

         if (xrotation < -this.xmaximum * 0.017453292F) {
            xrotation = -this.xmaximum * 0.017453292F;
         }
      }

      this.head.setValue(xrotation, EnumGeomData.xrot);
      float yrotation = -this.yoffset + this.yrD + this.headStartAngleY;
      if (this.ymaximum != 0.0F) {
         if (yrotation > this.ymaximum * 0.017453292F) {
            yrotation = this.ymaximum * 0.017453292F;
         }

         if (yrotation < -this.ymaximum * 0.017453292F) {
            yrotation = -this.ymaximum * 0.017453292F;
         }
      }

      if (this.headAxis == 1) {
         if (this.ydirection == 1) {
            this.head.setValue(yrotation, EnumGeomData.zrot);
         } else {
            this.head.setValue(-1.0F * yrotation, EnumGeomData.zrot);
            this.head.setValue(0.0F, EnumGeomData.yrot);
         }
      } else {
         if (this.ydirection == 1) {
            this.head.setValue(yrotation, EnumGeomData.yrot);
         } else {
            this.head.setValue(-1.0F * yrotation, EnumGeomData.yrot);
         }

         this.head.setValue(0.0F, EnumGeomData.zrot);
      }

      Iterator var9 = this.modules.iterator();

      while(var9.hasNext()) {
         Module m = (Module)var9.next();
         m.walk(entity, f, f1, f2, rotateAnglePitch, rotateAngleYaw);
      }

   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      this.walk(entity, f, f1, f2, f3, f4);
   }
}

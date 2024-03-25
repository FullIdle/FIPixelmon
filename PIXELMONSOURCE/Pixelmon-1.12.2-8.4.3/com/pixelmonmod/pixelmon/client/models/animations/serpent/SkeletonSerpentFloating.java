package com.pixelmonmod.pixelmon.client.models.animations.serpent;

import com.pixelmonmod.pixelmon.client.models.animations.EnumGeomData;
import com.pixelmonmod.pixelmon.client.models.animations.IModulized;
import com.pixelmonmod.pixelmon.client.models.animations.ModuleHead;
import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class SkeletonSerpentFloating extends SkeletonBase {
   public IModulized[] bodyParts;
   public int[] lengths;
   float animationAngle;
   float animationSpeed;
   float currentAngleX;
   float currentAngleZ;
   float topAngle;
   float dampeningFactor;
   float phaseoffset;

   public SkeletonSerpentFloating(ModelRenderer body, ModuleHead headModule, float animationAngle, float topAngle, float dampeningFactor, float animationSpeed, float phaseoffset, IModulized... bodyArgs) {
      super(body);
      this.modules.add(headModule);
      this.animationAngle = animationAngle;
      this.topAngle = topAngle;
      this.dampeningFactor = dampeningFactor;
      this.animationSpeed = animationSpeed;
      this.phaseoffset = phaseoffset;
      this.bodyParts = bodyArgs;
      this.lengths = new int[this.bodyParts.length];
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      for(int i = 0; i < this.bodyParts.length; ++i) {
         float xrD;
         float zrD;
         if (i == 0) {
            xrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.animationAngle)) * (float)Math.toRadians((double)this.topAngle) * MathHelper.func_76134_b(f2 * this.animationSpeed);
            zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.animationAngle)) * (float)Math.toRadians((double)this.topAngle) * MathHelper.func_76134_b(f2 * this.animationSpeed);
            this.bodyParts[i].setValue(xrD, EnumGeomData.xrot);
            this.bodyParts[i].setValue(zrD, EnumGeomData.zrot);
            this.currentAngleX = xrD;
            this.currentAngleZ = zrD;
         } else {
            xrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.animationAngle)) * ((float)Math.exp((double)(this.dampeningFactor * (float)i)) * (float)Math.toRadians((double)this.topAngle) * MathHelper.func_76134_b(f2 * this.animationSpeed + (float)i * this.phaseoffset) - this.currentAngleX);
            zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.animationAngle)) * ((float)Math.exp((double)(this.dampeningFactor * (float)i)) * (float)Math.toRadians((double)this.topAngle) * MathHelper.func_76134_b(f2 * this.animationSpeed + (float)i * this.phaseoffset) - this.currentAngleZ);
            this.bodyParts[i].setValue(xrD, EnumGeomData.xrot);
            this.bodyParts[i].setValue(zrD, EnumGeomData.zrot);
            this.currentAngleX += xrD;
            this.currentAngleZ += zrD;
         }
      }

   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      for(int i = 0; i < this.bodyParts.length; ++i) {
         float xrD;
         float zrD;
         if (i == 0) {
            xrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.animationAngle)) * (float)Math.toRadians((double)this.topAngle) * MathHelper.func_76134_b(f2 * this.animationSpeed);
            zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.animationAngle)) * (float)Math.toRadians((double)this.topAngle) * MathHelper.func_76134_b(f2 * this.animationSpeed);
            this.bodyParts[i].setValue(xrD, EnumGeomData.xrot);
            this.bodyParts[i].setValue(zrD, EnumGeomData.zrot);
            this.currentAngleX = xrD;
            this.currentAngleZ = zrD;
         } else {
            xrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.animationAngle)) * ((float)Math.exp((double)(this.dampeningFactor * (float)i)) * (float)Math.toRadians((double)this.topAngle) * MathHelper.func_76134_b(f2 * this.animationSpeed + (float)i * this.phaseoffset) - this.currentAngleX);
            zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.animationAngle)) * ((float)Math.exp((double)(this.dampeningFactor * (float)i)) * (float)Math.toRadians((double)this.topAngle) * MathHelper.func_76134_b(f2 * this.animationSpeed + (float)i * this.phaseoffset) - this.currentAngleZ);
            this.bodyParts[i].setValue(xrD, EnumGeomData.xrot);
            this.bodyParts[i].setValue(zrD, EnumGeomData.zrot);
            this.currentAngleX += xrD;
            this.currentAngleZ += zrD;
         }
      }

   }
}

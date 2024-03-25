package com.pixelmonmod.pixelmon.client.models.animations.serpent;

import com.pixelmonmod.pixelmon.client.models.animations.ModuleHead;
import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class SkeletonSerpent extends SkeletonBase {
   public ModelRenderer[] bodyParts;
   public int[] lengths;
   float animationAngle;
   float animationSpeed;
   float currentAngleY;
   float currentAngleX;
   float topAngle;
   float dampeningFactor;
   float phaseoffset;

   public SkeletonSerpent(ModelRenderer body, ModuleHead headModule, float animationAngle, float topAngle, float dampeningFactor, float animationSpeed, float phaseoffset, ModelRenderer... bodyArgs) {
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
         if (i == 0) {
            this.bodyParts[i].field_78796_g = MathHelper.func_76134_b((float)Math.toRadians((double)this.animationAngle)) * this.topAngle * MathHelper.func_76134_b(f * this.animationSpeed);
            this.currentAngleY = this.bodyParts[i].field_78796_g;
            this.bodyParts[i].field_78795_f = MathHelper.func_76126_a((float)Math.toRadians((double)this.animationAngle)) * this.topAngle * MathHelper.func_76134_b(f * this.animationSpeed);
            this.currentAngleX = this.bodyParts[i].field_78795_f;
         } else {
            this.bodyParts[i].field_78796_g = MathHelper.func_76134_b((float)Math.toRadians((double)this.animationAngle)) * ((float)Math.exp((double)(this.dampeningFactor * (float)i)) * this.topAngle * MathHelper.func_76134_b(f * this.animationSpeed + (float)i * this.phaseoffset) - this.currentAngleY);
            this.bodyParts[i].field_78795_f = MathHelper.func_76126_a((float)Math.toRadians((double)this.animationAngle)) * ((float)Math.exp((double)(this.dampeningFactor * (float)i)) * this.topAngle * MathHelper.func_76134_b(f * this.animationSpeed + (float)i * this.phaseoffset) - this.currentAngleX);
            this.currentAngleY += this.bodyParts[i].field_78796_g;
            this.currentAngleX += this.bodyParts[i].field_78795_f;
         }
      }

   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      for(int i = 0; i < this.bodyParts.length; ++i) {
         if (i == 0) {
            this.bodyParts[i].field_78796_g = MathHelper.func_76134_b((float)Math.toRadians((double)this.animationAngle)) * this.topAngle * MathHelper.func_76134_b(f * this.animationSpeed);
            this.currentAngleY = this.bodyParts[i].field_78796_g;
            this.bodyParts[i].field_78795_f = MathHelper.func_76126_a((float)Math.toRadians((double)this.animationAngle)) * this.topAngle * MathHelper.func_76134_b(f * this.animationSpeed);
            this.currentAngleX = this.bodyParts[i].field_78795_f;
         } else {
            this.bodyParts[i].field_78796_g = MathHelper.func_76134_b((float)Math.toRadians((double)this.animationAngle)) * ((float)Math.exp((double)(this.dampeningFactor * (float)i)) * this.topAngle * MathHelper.func_76134_b(f * this.animationSpeed + (float)i * this.phaseoffset) - this.currentAngleY);
            this.bodyParts[i].field_78795_f = MathHelper.func_76126_a((float)Math.toRadians((double)this.animationAngle)) * ((float)Math.exp((double)(this.dampeningFactor * (float)i)) * this.topAngle * MathHelper.func_76134_b(f * this.animationSpeed + (float)i * this.phaseoffset) - this.currentAngleX);
            this.currentAngleY += this.bodyParts[i].field_78796_g;
            this.currentAngleX += this.bodyParts[i].field_78795_f;
         }
      }

   }
}

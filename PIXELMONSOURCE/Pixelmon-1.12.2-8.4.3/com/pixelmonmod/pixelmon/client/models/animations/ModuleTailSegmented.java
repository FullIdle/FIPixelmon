package com.pixelmonmod.pixelmon.client.models.animations;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleTailSegmented extends ModuleTailBasic {
   public ModelRenderer[] tailParts;
   public int[] lengths;
   float phaseOffset;
   float zeroPoint;
   public float initialRotation;
   float tailRotationLimit;
   int tailType;
   float currentAngleY;

   public ModuleTailSegmented(ModelRenderer tail, int tailType, float zeroPoint, float initialRotation, float tailRotationLimit, float TailSpeed, float phaseOffset, ModelRenderer... tailArgs) {
      super(tail, 0.0F, 0.0F, TailSpeed);
      this.tailType = tailType;
      this.tailParts = tailArgs;
      this.phaseOffset = phaseOffset;
      this.zeroPoint = zeroPoint;
      this.initialRotation = initialRotation;
      this.tailRotationLimit = tailRotationLimit;
      this.TailSpeed = TailSpeed;
      this.lengths = new int[this.tailParts.length];
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      int i;
      if (this.tailType == 0) {
         for(i = 0; i < this.tailParts.length; ++i) {
            if (i == 0) {
               this.tailParts[i].field_78795_f = MathHelper.func_76126_a(f2 * this.TailSpeed) * MathHelper.func_76126_a(f2 * this.TailSpeed) * this.tailRotationLimit / (float)this.tailParts.length + this.initialRotation;
            } else {
               this.tailParts[i].field_78795_f = MathHelper.func_76126_a(f2 * this.TailSpeed) * MathHelper.func_76126_a(f2 * this.TailSpeed) * this.tailRotationLimit / (float)this.tailParts.length;
            }
         }
      } else {
         for(i = 0; i < this.tailParts.length; ++i) {
            if (i == 0) {
               this.tailParts[i].field_78795_f = MathHelper.func_76126_a(f2 * this.TailSpeed) * this.tailRotationLimit / (float)this.tailParts.length + this.initialRotation;
               this.tailParts[i].field_78796_g = this.tailRotationLimit / 3.0F * MathHelper.func_76134_b(f2 * this.TailSpeed);
               this.currentAngleY = this.tailParts[i].field_78796_g;
            } else {
               this.tailParts[i].field_78795_f = MathHelper.func_76126_a(f2 * this.TailSpeed) * this.tailRotationLimit / (float)this.tailParts.length;
               this.tailParts[i].field_78796_g = (float)Math.exp(-0.1 * (double)i) * this.tailRotationLimit / 2.0F * MathHelper.func_76134_b(f2 * this.TailSpeed - (float)i) - this.currentAngleY;
               this.currentAngleY += this.tailParts[i].field_78796_g;
            }
         }
      }

   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      this.walk(entity, f, f1, f2, f3, f4);
      if (this.tailType == 0) {
         for(int i = 0; i < this.tailParts.length; ++i) {
            if (i == 0) {
               this.tailParts[i].field_78795_f = MathHelper.func_76126_a(f2 * this.TailSpeed) * MathHelper.func_76126_a(f2 * this.TailSpeed) * this.tailRotationLimit / (float)this.tailParts.length + this.initialRotation;
            } else {
               this.tailParts[i].field_78795_f = MathHelper.func_76126_a(f2 * this.TailSpeed) * MathHelper.func_76126_a(f2 * this.TailSpeed) * this.tailRotationLimit / (float)this.tailParts.length;
            }
         }
      }

   }
}

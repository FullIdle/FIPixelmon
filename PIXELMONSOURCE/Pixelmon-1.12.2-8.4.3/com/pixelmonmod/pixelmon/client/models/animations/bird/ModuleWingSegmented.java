package com.pixelmonmod.pixelmon.client.models.animations.bird;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleWingSegmented extends ModuleWing {
   public ModelRenderer[] wingParts;
   public int[] lengths;
   float phaseOffset;
   float zeroPoint;
   float initialRotation;

   public ModuleWingSegmented(ModelRenderer wing, EnumWing WingVariable, float WingOrientation, float zeroPoint, float initialRotation, float WingRotationLimit, float WingSpeed, float phaseOffset, ModelRenderer... wingArgs) {
      super(wing, WingVariable, WingOrientation, WingRotationLimit, WingSpeed);
      this.wingParts = wingArgs;
      this.phaseOffset = phaseOffset;
      this.zeroPoint = zeroPoint;
      this.initialRotation = initialRotation;
      this.lengths = new int[this.wingParts.length];
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      for(int i = 0; i < this.wingParts.length; ++i) {
         if (i == 0) {
            this.wingParts[i].field_78796_g = MathHelper.func_76134_b((float)Math.toRadians((double)this.WingOrientation)) * this.WingDirection * ((float)Math.pow((double)MathHelper.func_76134_b(f2 * this.WingSpeed - (float)i / this.phaseOffset), 2.0) * 3.1415927F - this.zeroPoint) * this.WingRotationLimit / (float)this.wingParts.length + this.WingDirection * (float)((double)MathHelper.func_76134_b((float)Math.toRadians((double)this.WingOrientation)) * Math.toRadians((double)this.initialRotation));
            this.wingParts[i].field_78808_h = MathHelper.func_76126_a((float)Math.toRadians((double)this.WingOrientation)) * this.WingDirection * ((float)Math.pow((double)MathHelper.func_76134_b(f2 * this.WingSpeed - (float)i / this.phaseOffset), 2.0) * 3.1415927F - this.zeroPoint) * this.WingRotationLimit / (float)this.wingParts.length + this.WingDirection * (float)((double)MathHelper.func_76126_a((float)Math.toRadians((double)this.WingOrientation)) * Math.toRadians((double)this.initialRotation));
         } else {
            this.wingParts[i].field_78796_g = MathHelper.func_76134_b((float)Math.toRadians((double)this.WingOrientation)) * this.WingDirection * ((float)Math.pow((double)MathHelper.func_76134_b(f2 * this.WingSpeed - (float)i / this.phaseOffset), 2.0) * 3.1415927F - this.zeroPoint) * this.WingRotationLimit / (float)(this.wingParts.length * i);
            this.wingParts[i].field_78808_h = MathHelper.func_76126_a((float)Math.toRadians((double)this.WingOrientation)) * this.WingDirection * ((float)Math.pow((double)MathHelper.func_76134_b(f2 * this.WingSpeed - (float)i / this.phaseOffset), 2.0) * 3.1415927F - this.zeroPoint) * this.WingRotationLimit / (float)(this.wingParts.length * i);
         }
      }

   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      this.walk(entity, f, f1, f2, f3, f4);
   }
}

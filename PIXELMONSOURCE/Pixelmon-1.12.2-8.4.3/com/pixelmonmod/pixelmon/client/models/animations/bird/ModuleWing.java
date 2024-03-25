package com.pixelmonmod.pixelmon.client.models.animations.bird;

import com.pixelmonmod.pixelmon.client.models.animations.EnumGeomData;
import com.pixelmonmod.pixelmon.client.models.animations.IModulized;
import com.pixelmonmod.pixelmon.client.models.animations.Module;
import com.pixelmonmod.pixelmon.client.models.animations.ModulizedRenderWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleWing extends Module {
   IModulized wing;
   float WingRotationLimit;
   float WingSpeed;
   float WingInitY;
   float WingInitZ;
   float WingDirection;
   float WingOrientation;
   EnumWing WingVariable;

   public ModuleWing(IModulized wing, EnumWing WingVariable, float WingOrientation, float WingRotationLimit, float WingSpeed) {
      this.wing = wing;
      this.WingSpeed = WingSpeed;
      this.WingRotationLimit = WingRotationLimit;
      this.WingOrientation = WingOrientation;
      this.WingInitY = wing.getValue(EnumGeomData.yrot);
      this.WingInitZ = wing.getValue(EnumGeomData.zrot);
      this.WingVariable = WingVariable;
      if (WingVariable == EnumWing.Right) {
         this.WingDirection = 1.0F;
      } else {
         this.WingDirection = -1.0F;
      }

   }

   public ModuleWing(ModelRenderer wing, EnumWing WingVariable, float WingOrientation, float WingRotationLimit, float WingSpeed) {
      this((IModulized)(new ModulizedRenderWrapper(wing)), WingVariable, WingOrientation, WingRotationLimit, WingSpeed);
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      this.yrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.WingOrientation)) * this.WingDirection * MathHelper.func_76134_b(f2 * this.WingSpeed) * 3.1415927F * this.WingRotationLimit;
      this.zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.WingOrientation)) * this.WingDirection * MathHelper.func_76134_b(f2 * this.WingSpeed) * 3.1415927F * this.WingRotationLimit;
      this.wing.setValue(this.yrD, EnumGeomData.yrot);
      this.wing.setValue(this.zrD, EnumGeomData.zrot);
   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      this.walk(entity, f, f1, f2, f3, f4);
   }
}

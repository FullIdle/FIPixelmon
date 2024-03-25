package com.pixelmonmod.pixelmon.client.models.animations.fish;

import com.pixelmonmod.pixelmon.client.models.animations.EnumArm;
import com.pixelmonmod.pixelmon.client.models.animations.EnumGeomData;
import com.pixelmonmod.pixelmon.client.models.animations.IModulized;
import com.pixelmonmod.pixelmon.client.models.animations.Module;
import com.pixelmonmod.pixelmon.client.models.animations.ModulizedRenderWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleFin extends Module {
   IModulized Fin;
   float FinRotationLimit;
   float FinInitY;
   float FinInitZ;
   float FinSpeed;
   float FinDirection;
   float FinOrientation;
   EnumArm FinVariable;

   public ModuleFin(IModulized Fin, EnumArm FinVariable, float FinOrientation, float FinRotationLimit, float FinSpeed) {
      this.Fin = Fin;
      this.FinRotationLimit = FinRotationLimit;
      this.FinOrientation = FinOrientation;
      this.FinSpeed = FinSpeed;
      this.FinInitY = Fin.getValue(EnumGeomData.yrot);
      this.FinInitZ = Fin.getValue(EnumGeomData.zrot);
      if (FinVariable == EnumArm.Right) {
         this.FinDirection = 1.0F;
      } else {
         this.FinDirection = -1.0F;
      }

   }

   public ModuleFin(ModelRenderer Fin, EnumArm FinVariable, float FinOrientation, float FinRotationLimit, float FinSpeed) {
      this((IModulized)(new ModulizedRenderWrapper(Fin)), FinVariable, FinOrientation, FinRotationLimit, FinSpeed);
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      this.yrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.FinOrientation)) * this.FinDirection * MathHelper.func_76134_b(f * this.FinSpeed) * this.FinRotationLimit * f1;
      this.zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.FinOrientation)) * this.FinDirection * MathHelper.func_76134_b(f * this.FinSpeed) * this.FinRotationLimit * f1;
      this.Fin.setValue(this.yrD + this.FinInitY, EnumGeomData.yrot);
      this.Fin.setValue(this.zrD + this.FinInitZ, EnumGeomData.zrot);
   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
   }
}

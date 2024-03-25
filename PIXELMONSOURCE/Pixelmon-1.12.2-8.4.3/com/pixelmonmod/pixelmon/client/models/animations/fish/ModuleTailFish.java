package com.pixelmonmod.pixelmon.client.models.animations.fish;

import com.pixelmonmod.pixelmon.client.models.animations.EnumGeomData;
import com.pixelmonmod.pixelmon.client.models.animations.IModulized;
import com.pixelmonmod.pixelmon.client.models.animations.Module;
import com.pixelmonmod.pixelmon.client.models.animations.ModulizedRenderWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleTailFish extends Module {
   IModulized tail;
   float TailRotationLimit;
   float TailSpeed;
   float TailInitY;
   float TailInitZ;
   float TailOrientation;
   float TurningSpeed;

   public ModuleTailFish(IModulized tail, float TailOrientation, float TailRotationLimit, float TailSpeed) {
      this.tail = tail;
      this.TailSpeed = TailSpeed;
      this.TailRotationLimit = TailRotationLimit;
      this.TailOrientation = TailOrientation;
      this.TailInitY = tail.getValue(EnumGeomData.yrot);
      this.TailInitZ = tail.getValue(EnumGeomData.zrot);
   }

   public ModuleTailFish(ModelRenderer tail, float TailOrientation, float TailRotationLimit, float TailSpeed) {
      this((IModulized)(new ModulizedRenderWrapper(tail)), TailOrientation, TailRotationLimit, TailSpeed);
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      this.yrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.TailOrientation)) * MathHelper.func_76134_b(f * this.TailSpeed) * 3.1415927F * f1 * this.TailRotationLimit;
      this.xrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.TailOrientation)) * MathHelper.func_76134_b(f * this.TailSpeed) * 3.1415927F * f1 * this.TailRotationLimit;
      this.tail.setValue(this.yrD + this.TailInitY, EnumGeomData.yrot);
      this.tail.setValue(this.xrD + this.TailInitY, EnumGeomData.xrot);
   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
   }
}

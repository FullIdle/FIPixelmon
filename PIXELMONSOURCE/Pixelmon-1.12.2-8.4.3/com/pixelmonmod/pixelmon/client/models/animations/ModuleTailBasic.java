package com.pixelmonmod.pixelmon.client.models.animations;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import java.util.Iterator;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleTailBasic extends Module {
   IModulized tail;
   float TailRotationLimitY;
   float TailRotationLimitX;
   float TailSpeed;
   float TailInitY;
   float TailInitX;
   float TailOrientation;
   float TurningSpeed;
   float TurningAngle;

   public ModuleTailBasic(IModulized tail, float TailRotationLimitY, float TailRotationLimitX, float TailSpeed) {
      this.tail = tail;
      this.TailSpeed = TailSpeed;
      this.TailRotationLimitY = TailRotationLimitY;
      this.TailRotationLimitX = TailRotationLimitX;
      this.TailInitY = this.tail.getValue(EnumGeomData.yrot);
      this.TailInitX = this.tail.getValue(EnumGeomData.xrot);
   }

   public ModuleTailBasic(ModelRenderer tail, float TailRotationLimitY, float TailRotationLimitX, float TailSpeed) {
      this((IModulized)(new ModulizedRenderWrapper(tail)), TailRotationLimitY, TailRotationLimitX, TailSpeed);
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      this.TurningSpeed = Math.abs(entity.field_70177_z - entity.field_70126_B);
      this.yrD = MathHelper.func_76134_b(f * this.TailSpeed) * 3.1415927F * f1 * this.TailRotationLimitY + this.TurningAngle;
      this.xrD = MathHelper.func_76134_b(f * this.TailSpeed * 2.0F) * 3.1415927F * f1 * this.TailRotationLimitX;
      this.tail.setValue(this.yrD, EnumGeomData.yrot);
      this.tail.setValue(this.xrD, EnumGeomData.xrot);
      Iterator var7 = this.modules.iterator();

      while(var7.hasNext()) {
         Module m = (Module)var7.next();
         m.walk(entity, f, f1, f2, f3, f4);
      }

   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
   }
}

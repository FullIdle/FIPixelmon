package com.pixelmonmod.pixelmon.client.models.animations;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import java.util.Iterator;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleArm extends Module {
   IModulized arm;
   float ArmRotationLimit;
   float ArmSpeed;
   float ArmInitX;
   float ArmInitY;
   float ArmInitZ;
   float ArmDirection;
   EnumArm ArmVariable;
   EnumRotation rotationAxis;

   public ModuleArm(IModulized arm, EnumArm ArmVariable, EnumRotation rotationAxis, float ArmRotationLimit, float ArmSpeed) {
      this.arm = arm;
      this.ArmSpeed = ArmSpeed;
      this.ArmRotationLimit = ArmRotationLimit;
      this.ArmInitY = this.arm.getValue(EnumGeomData.zrot);
      this.ArmInitZ = this.arm.getValue(EnumGeomData.zrot);
      this.ArmInitX = this.arm.getValue(EnumGeomData.xrot);
      this.ArmVariable = ArmVariable;
      this.rotationAxis = rotationAxis;
      if (ArmVariable == EnumArm.Right) {
         this.ArmDirection = 1.0F;
      } else {
         this.ArmDirection = -1.0F;
      }

   }

   public ModuleArm(ModelRenderer arm, EnumArm ArmVariable, EnumRotation rotationAxis, float ArmRotationLimit, float ArmSpeed) {
      this((IModulized)(new ModulizedRenderWrapper(arm)), ArmVariable, rotationAxis, ArmRotationLimit, ArmSpeed);
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      switch (this.rotationAxis) {
         case x:
            this.xrD = MathHelper.func_76134_b(f * this.ArmSpeed + 3.1415927F) * this.ArmRotationLimit * f1 * this.ArmDirection;
            this.arm.setValue(this.xrD + this.ArmInitX, EnumGeomData.xrot);
            break;
         case y:
            this.yrD = MathHelper.func_76134_b(f * this.ArmSpeed + 3.1415927F) * this.ArmRotationLimit * f1 * this.ArmDirection;
            this.arm.setValue(this.yrD + this.ArmInitY, EnumGeomData.yrot);
            break;
         case z:
            this.zrD = MathHelper.func_76134_b(f * this.ArmSpeed + 3.1415927F) * this.ArmRotationLimit * f1 * this.ArmDirection;
            this.arm.setValue(this.zrD + this.ArmInitZ, EnumGeomData.zrot);
      }

      Iterator var7 = this.modules.iterator();

      while(var7.hasNext()) {
         Module m = (Module)var7.next();
         m.walk(entity, f, f1, f2, f3, f4);
      }

   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
   }
}

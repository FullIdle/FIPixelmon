package com.pixelmonmod.pixelmon.client.models.animations;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import java.util.Iterator;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleLeg extends Module {
   IModulized leg;
   float WalkOffset;
   float LegRotationLimit;
   float LegInitX;
   float LegInitY;
   float LegInitZ;
   float legSpeed;
   EnumRotation rotationAxis;
   EnumPhase phaseVariable;
   EnumLeg legVariable;

   public ModuleLeg(IModulized leg, EnumLeg legVariable, EnumPhase phaseVariable, EnumRotation rotationAxis, float LegRotationLimit, float legSpeed) {
      this.leg = leg;
      this.LegRotationLimit = LegRotationLimit;
      this.legSpeed = legSpeed;
      this.phaseVariable = phaseVariable;
      this.legVariable = legVariable;
      this.rotationAxis = rotationAxis;
      this.LegInitX = this.leg.getValue(EnumGeomData.xrot);
      this.LegInitY = this.leg.getValue(EnumGeomData.yrot);
      this.LegInitZ = this.leg.getValue(EnumGeomData.zrot);
      if (phaseVariable == EnumPhase.InPhase) {
         if (legVariable == EnumLeg.FrontLeft) {
            this.WalkOffset = 3.1415927F;
         } else if (legVariable == EnumLeg.FrontRight) {
            this.WalkOffset = 0.0F;
         } else if (legVariable == EnumLeg.BackLeft) {
            this.WalkOffset = 3.1415927F;
         } else {
            this.WalkOffset = 0.0F;
         }
      } else if (legVariable == EnumLeg.FrontLeft) {
         this.WalkOffset = 3.1415927F;
      } else if (legVariable == EnumLeg.FrontRight) {
         this.WalkOffset = 0.0F;
      } else if (legVariable == EnumLeg.BackLeft) {
         this.WalkOffset = 0.0F;
      } else {
         this.WalkOffset = 3.1415927F;
      }

   }

   public ModuleLeg(ModelRenderer leg, EnumLeg legVariable, EnumPhase phaseVariable, EnumRotation rotationAxis, float LegRotationLimit, float legSpeed) {
      this((IModulized)(new ModulizedRenderWrapper(leg)), legVariable, phaseVariable, rotationAxis, LegRotationLimit, legSpeed);
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      switch (this.rotationAxis) {
         case x:
            this.xrD = MathHelper.func_76134_b(f * this.legSpeed + this.WalkOffset) * this.LegRotationLimit * f1;
            this.leg.setValue(this.xrD + this.LegInitX, EnumGeomData.xrot);
            break;
         case y:
            this.yrD = MathHelper.func_76134_b(f * this.legSpeed + this.WalkOffset) * this.LegRotationLimit * f1;
            this.leg.setValue(this.yrD + this.LegInitY, EnumGeomData.yrot);
            break;
         case z:
            this.zrD = MathHelper.func_76134_b(f * this.legSpeed + this.WalkOffset) * this.LegRotationLimit * f1;
            this.leg.setValue(this.zrD + this.LegInitZ, EnumGeomData.zrot);
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

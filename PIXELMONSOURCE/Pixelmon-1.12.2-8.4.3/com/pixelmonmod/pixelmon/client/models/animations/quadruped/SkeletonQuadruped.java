package com.pixelmonmod.pixelmon.client.models.animations.quadruped;

import com.pixelmonmod.pixelmon.client.models.animations.Module;
import com.pixelmonmod.pixelmon.client.models.animations.ModuleLeg;
import com.pixelmonmod.pixelmon.client.models.animations.ModuleTailBasic;
import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import net.minecraft.client.model.ModelRenderer;

public class SkeletonQuadruped extends SkeletonBase {
   public SkeletonQuadruped(ModelRenderer body, Module headModule, ModuleLeg frontLeftLeg, ModuleLeg frontRightLeg, ModuleLeg backLeftLeg, ModuleLeg backRightLeg, ModuleTailBasic tail) {
      super(body);
      if (headModule != null) {
         this.modules.add(headModule);
      }

      if (frontLeftLeg != null) {
         this.modules.add(frontLeftLeg);
      }

      if (frontRightLeg != null) {
         this.modules.add(frontRightLeg);
      }

      if (backLeftLeg != null) {
         this.modules.add(backLeftLeg);
      }

      if (backRightLeg != null) {
         this.modules.add(backRightLeg);
      }

      if (tail != null) {
         this.modules.add(tail);
      }

   }
}

package com.pixelmonmod.pixelmon.client.models.animations.biped;

import com.pixelmonmod.pixelmon.client.models.animations.ModuleArm;
import com.pixelmonmod.pixelmon.client.models.animations.ModuleHead;
import com.pixelmonmod.pixelmon.client.models.animations.ModuleLeg;
import com.pixelmonmod.pixelmon.client.models.animations.ModuleTailBasic;
import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import net.minecraft.client.model.ModelRenderer;

public class SkeletonBiped extends SkeletonBase {
   public SkeletonBiped(ModelRenderer body, ModuleHead headModule, ModuleArm leftArm, ModuleArm rightArm, ModuleLeg leftLeg, ModuleLeg rightLeg, ModuleTailBasic tail) {
      super(body);
      if (headModule != null) {
         this.modules.add(headModule);
      }

      if (leftLeg != null) {
         this.modules.add(leftLeg);
      }

      if (rightLeg != null) {
         this.modules.add(rightLeg);
      }

      if (rightArm != null) {
         this.modules.add(rightArm);
      }

      if (leftArm != null) {
         this.modules.add(leftArm);
      }

      if (tail != null) {
         this.modules.add(tail);
      }

   }
}

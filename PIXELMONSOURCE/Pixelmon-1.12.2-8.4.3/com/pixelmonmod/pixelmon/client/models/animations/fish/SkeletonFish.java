package com.pixelmonmod.pixelmon.client.models.animations.fish;

import com.pixelmonmod.pixelmon.client.models.animations.Module;
import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import net.minecraft.client.model.ModelRenderer;

public class SkeletonFish extends SkeletonBase {
   public SkeletonFish(ModelRenderer body, Module headModule, ModuleFin LeftFrontFin, ModuleFin RightFrontFin, ModuleFin LeftBackFin, ModuleFin RightBackFin, ModuleTailFish tail) {
      super(body);
      if (headModule != null) {
         this.modules.add(headModule);
      }

      if (LeftFrontFin != null) {
         this.modules.add(LeftFrontFin);
      }

      if (RightFrontFin != null) {
         this.modules.add(RightFrontFin);
      }

      if (LeftBackFin != null) {
         this.modules.add(LeftBackFin);
      }

      if (RightBackFin != null) {
         this.modules.add(RightBackFin);
      }

      if (tail != null) {
         this.modules.add(tail);
      }

   }
}

package com.pixelmonmod.pixelmon.client.models.animations.bird;

import com.pixelmonmod.pixelmon.client.models.animations.ModuleHead;
import com.pixelmonmod.pixelmon.client.models.animations.ModuleTailBasic;
import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import net.minecraft.client.model.ModelRenderer;

public class SkeletonBird extends SkeletonBase {
   public SkeletonBird(ModelRenderer body, ModuleHead head, ModuleWing leftWing, ModuleWing rightWing, ModelRenderer leftLeg, ModelRenderer rightLeg) {
      super(body);
      if (head != null) {
         this.modules.add(head);
      }

      if (leftWing != null) {
         this.modules.add(leftWing);
      }

      if (rightWing != null) {
         this.modules.add(rightWing);
      }

   }

   public SkeletonBird(ModelRenderer body, ModuleHead head, ModuleWing leftWing, ModuleWing rightWing, ModelRenderer leftLeg, ModelRenderer rightLeg, ModuleTailBasic tail) {
      super(body);
      if (head != null) {
         this.modules.add(head);
      }

      if (leftWing != null) {
         this.modules.add(leftWing);
      }

      if (rightWing != null) {
         this.modules.add(rightWing);
      }

      if (tail != null) {
         this.modules.add(tail);
      }

   }
}

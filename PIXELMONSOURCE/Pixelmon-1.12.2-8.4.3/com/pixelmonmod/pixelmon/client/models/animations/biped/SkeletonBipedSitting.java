package com.pixelmonmod.pixelmon.client.models.animations.biped;

import com.pixelmonmod.pixelmon.client.models.animations.ModuleHead;
import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import net.minecraft.client.model.ModelRenderer;

public class SkeletonBipedSitting extends SkeletonBase {
   ModelRenderer LeftArm;
   ModelRenderer RightArm;

   public SkeletonBipedSitting(ModelRenderer body, ModuleHead headModule, ModelRenderer LeftArm, ModelRenderer RightArm, ModelRenderer leftLeg, ModelRenderer rightLeg) {
      super(body);
      this.modules.add(headModule);
      this.LeftArm = LeftArm;
      this.RightArm = RightArm;
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      super.walk(entity, f, f1, f2, f3, f4);
   }
}

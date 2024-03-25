package com.pixelmonmod.pixelmon.client.models.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelMechanicalAnvil extends GenericSmdModel {
   public ModelMechanicalAnvil() {
      super("models/blocks/mech_anvil", "mech_anvil.pqc");
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      float scale = 0.2654421F;
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
   }
}

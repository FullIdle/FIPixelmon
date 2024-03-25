package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.client.models.ModelHolder;
import com.pixelmonmod.pixelmon.client.models.bikes.ModelBike;
import com.pixelmonmod.pixelmon.client.render.tileEntities.SharedModels;
import com.pixelmonmod.pixelmon.entities.bikes.EntityBike;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBike extends Render {
   public RenderBike(RenderManager renderManager) {
      super(renderManager);
   }

   public void doRender(EntityBike bike, double x, double y, double z, float f, float f1) {
      ModelHolder model = SharedModels.getBikeModel(bike.getType());
      if (model.getModel() != null) {
         ((ModelBike)model.getModel()).setupForRender(bike);
         ((ModelBike)model.getModel()).theModel.animate();
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b(x, y, z);
         GlStateManager.func_179114_b(-bike.field_70177_z, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.func_110776_a(new ResourceLocation("pixelmon:models/bikes/bikes_" + bike.getColor().func_176610_l() + ".png"));
         GlStateManager.func_179114_b(((ModelBike)model.getModel()).theModel.currentSequence != null && ((ModelBike)model.getModel()).theModel.currentSequence.current() != null ? 90.0F : 180.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.func_179094_E();
         GlStateManager.func_179091_B();
         ((ModelBike)model.getModel()).render(bike, 0.6F);
         GlStateManager.func_179101_C();
         GlStateManager.func_179121_F();
         GlStateManager.func_179121_F();
      }
   }

   protected ResourceLocation getEntityTexture(EntityBike entity) {
      return null;
   }
}

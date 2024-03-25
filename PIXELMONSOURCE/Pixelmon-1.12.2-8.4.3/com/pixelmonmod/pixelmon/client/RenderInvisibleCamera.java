package com.pixelmonmod.pixelmon.client;

import com.pixelmonmod.pixelmon.client.camera.EntityCamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderInvisibleCamera extends Render {
   public RenderInvisibleCamera(RenderManager renderManager) {
      super(renderManager);
   }

   public void doRender(EntityCamera var1, double var2, double var4, double var6, float var8, float var9) {
   }

   protected ResourceLocation getEntityTexture(EntityCamera entity) {
      return null;
   }
}

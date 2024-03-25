package com.pixelmonmod.pixelmon.client.models;

import net.minecraft.client.renderer.GlStateManager;

public class ModelCustomWrapper {
   public IPixelmonModel model;
   int frame = 0;
   float offsetX = 0.0F;
   float offsetY = 0.0F;
   float offsetZ = 0.0F;

   public ModelCustomWrapper(IPixelmonModel m) {
      this.model = m;
   }

   public ModelCustomWrapper(IPixelmonModel m, float x, float y, float z) {
      this.model = m;
      this.offsetX = x;
      this.offsetY = y;
      this.offsetZ = z;
   }

   public ModelCustomWrapper setOffsets(float x, float y, float z) {
      this.offsetX = x;
      this.offsetY = y;
      this.offsetZ = z;
      return this;
   }

   public void render(float scale, float partialTick) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(scale, scale, scale);
      this.model.renderAll(partialTick);
      GlStateManager.func_179121_F();
   }

   public void renderOffset(float scale, float partialTick) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(scale, scale, scale);
      GlStateManager.func_179109_b(this.offsetX, this.offsetZ, this.offsetY);
      this.model.renderAll(partialTick);
   }
}

package com.pixelmonmod.pixelmon.client.render.player;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerWrapper implements LayerRenderer {
   public final LayerRenderer renderer;
   private double offsetX;
   private double offsetY;
   private double offsetZ;
   private boolean enabled = true;

   public LayerWrapper(LayerRenderer renderer1) {
      this.renderer = renderer1;
   }

   public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (this.enabled) {
         GlStateManager.func_179094_E();
         if (this.offsetX != 0.0 || this.offsetY != 0.0 || this.offsetZ != 0.0) {
            GlStateManager.func_179137_b(this.offsetX, this.offsetY, this.offsetZ);
         }

         this.renderer.func_177141_a(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
         GlStateManager.func_179121_F();
      }
   }

   public boolean func_177142_b() {
      return this.renderer.func_177142_b();
   }

   public LayerWrapper setTranslates(double offsetX, double offsetY, double offsetZ) {
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      this.offsetZ = offsetZ;
      return this;
   }

   public LayerWrapper setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }
}

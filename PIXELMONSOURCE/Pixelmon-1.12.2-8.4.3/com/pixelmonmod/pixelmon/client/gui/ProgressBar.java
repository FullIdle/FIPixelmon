package com.pixelmonmod.pixelmon.client.gui;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ProgressBar {
   private int value;
   private int maxValue = 100;

   public void setProgress(int progress) {
      this.value = progress;
   }

   public void draw(int x, int y, int height, int width, int screenWidth, int screenHeight) {
      GlStateManager.func_179091_B();
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexbuffer = tessellator.func_178180_c();
      GlStateManager.func_179090_x();
      vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      vertexbuffer.func_181662_b((double)(x - width / 2), (double)y, 0.0).func_181666_a(0.0F, 0.0F, 0.0F, 1.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(x - width / 2), (double)(y + height), 0.0).func_181666_a(0.0F, 0.0F, 0.0F, 1.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(x + width / 2), (double)(y + height), 0.0).func_181666_a(0.0F, 0.0F, 0.0F, 1.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(x + width / 2), (double)y, 0.0).func_181666_a(0.0F, 0.0F, 0.0F, 1.0F).func_181675_d();
      int barWidth = (int)((float)this.value / (float)this.maxValue * ((float)width - 6.0F));
      vertexbuffer.func_181662_b((double)(x - width / 2 + 3), (double)(y + 3), 0.0).func_181666_a(1.0F - (float)this.value / (float)this.maxValue * 0.8F, 0.2F + (float)this.value / (float)this.maxValue * 0.8F, 0.2F, 1.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(x - width / 2 + 3), (double)(y + height - 3), 0.0).func_181666_a(1.0F - (float)this.value / (float)this.maxValue * 0.8F, 0.2F + (float)this.value / (float)this.maxValue * 0.8F, 0.2F, 1.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(x - width / 2 + 3 + barWidth), (double)(y + height - 3), 0.0).func_181666_a(1.0F - (float)this.value / (float)this.maxValue * 0.8F, 0.2F + (float)this.value / (float)this.maxValue * 0.8F, 0.2F, 1.0F).func_181675_d();
      vertexbuffer.func_181662_b((double)(x - width / 2 + 3 + barWidth), (double)(y + 3), 0.0).func_181666_a(1.0F - (float)this.value / (float)this.maxValue * 0.8F, 0.2F + (float)this.value / (float)this.maxValue * 0.8F, 0.2F, 1.0F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179121_F();
      GlStateManager.func_179098_w();
      GlStateManager.func_179101_C();
      GlStateManager.func_179119_h();
   }
}

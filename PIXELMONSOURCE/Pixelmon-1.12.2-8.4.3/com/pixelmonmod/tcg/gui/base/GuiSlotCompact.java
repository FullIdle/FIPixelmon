package com.pixelmonmod.tcg.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

public abstract class GuiSlotCompact extends GuiSlot {
   protected GuiContextMenuSlotItem itemSlotContextMenu;

   public GuiSlotCompact(Minecraft mcIn, int width, int height, int posX, int posY, int slotHeightIn) {
      super(mcIn, width, height, posY, posY + height, slotHeightIn);
      this.field_148152_e = posX;
      this.field_148151_d = posX + width;
   }

   protected int func_148137_d() {
      return this.field_148151_d - 6;
   }

   public void func_178039_p() {
      if (this.func_148141_e(this.field_148162_h) && Mouse.getButtonCount() > 1 && Mouse.isButtonDown(1) && this.func_148125_i() && this.field_148162_h >= this.field_148153_b && this.field_148162_h <= this.field_148154_c) {
         int i = this.field_148155_a / 2 - this.func_148139_c() / 2;
         int j = this.field_148155_a / 2 + this.func_148139_c() / 2;
         int k = this.field_148162_h - this.field_148153_b - this.field_148160_j + (int)this.field_148169_q - 4;
         int l = k / this.field_148149_f;
         if (this.field_148150_g >= i && this.field_148150_g <= j && l >= 0 && k >= 0 && l < this.func_148127_b()) {
            this.elementRightClicked(l, this.field_148150_g, this.field_148162_h);
         }
      }

      super.func_178039_p();
   }

   public void elementRightClicked(int slotIndex, int mouseX, int mouseY) {
      this.field_148168_r = slotIndex;
      if (this.itemSlotContextMenu != null) {
         this.itemSlotContextMenu.x = mouseX;
         this.itemSlotContextMenu.y = mouseY - 5;
         this.itemSlotContextMenu.setVisible(true);
         this.func_148143_b(false);
      }

   }

   public void initGui() {
   }

   public void updateScreen() {
      if (this.itemSlotContextMenu != null) {
         this.itemSlotContextMenu.updateScreen();
      }

   }

   public void func_148128_a(int mouseXIn, int mouseYIn, float p_148128_3_) {
      if (this.field_178041_q) {
         this.field_148150_g = mouseXIn;
         this.field_148162_h = mouseYIn;
         this.func_148123_a();
         this.func_148121_k();
         GlStateManager.func_179140_f();
         GlStateManager.func_179106_n();
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder worldrenderer = tessellator.func_178180_c();
         this.drawContainerBackground(tessellator);
         int i1 = this.field_148152_e + this.field_148155_a / 2 - this.func_148139_c() / 2 + 2;
         int j1 = this.field_148153_b + 4 - (int)this.field_148169_q;
         if (this.field_148165_u) {
            this.func_148129_a(i1, j1, tessellator);
         }

         this.func_192638_a(i1, j1, mouseXIn, mouseYIn, p_148128_3_);
         GlStateManager.func_179097_i();
         byte b0 = 4;
         this.func_148136_c(this.field_148153_b - this.field_148149_f, this.field_148153_b, 255, 255);
         this.func_148136_c(this.field_148154_c, this.field_148154_c + this.field_148149_f, 255, 255);
         GlStateManager.func_179147_l();
         GlStateManager.func_179120_a(770, 771, 0, 1);
         GlStateManager.func_179118_c();
         GlStateManager.func_179103_j(7425);
         GlStateManager.func_179090_x();
         worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
         worldrenderer.func_181662_b((double)this.field_148152_e, (double)(this.field_148153_b + b0), 0.0).func_187315_a(0.0, 1.0).func_181669_b(0, 0, 0, 0).func_181675_d();
         worldrenderer.func_181662_b((double)this.field_148151_d, (double)(this.field_148153_b + b0), 0.0).func_187315_a(1.0, 1.0).func_181669_b(0, 0, 0, 0).func_181675_d();
         worldrenderer.func_181662_b((double)this.field_148151_d, (double)this.field_148153_b, 0.0).func_187315_a(1.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
         worldrenderer.func_181662_b((double)this.field_148152_e, (double)this.field_148153_b, 0.0).func_187315_a(0.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
         tessellator.func_78381_a();
         worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
         worldrenderer.func_181662_b((double)this.field_148152_e, (double)this.field_148154_c, 0.0).func_187315_a(0.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
         worldrenderer.func_181662_b((double)this.field_148151_d, (double)this.field_148154_c, 0.0).func_187315_a(1.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
         worldrenderer.func_181662_b((double)this.field_148151_d, (double)(this.field_148154_c - b0), 0.0).func_187315_a(1.0, 0.0).func_181669_b(0, 0, 0, 0).func_181675_d();
         worldrenderer.func_181662_b((double)this.field_148152_e, (double)(this.field_148154_c - b0), 0.0).func_187315_a(0.0, 0.0).func_181669_b(0, 0, 0, 0).func_181675_d();
         tessellator.func_78381_a();
         int k1 = this.func_148137_d();
         if (k1 > 0) {
            int scrollBarX = this.func_148137_d();
            int l = scrollBarX + 6;
            int scrollbarHeight = (this.field_148154_c - this.field_148153_b) * (this.field_148154_c - this.field_148153_b) / this.func_148138_e();
            scrollbarHeight = MathHelper.func_76125_a(scrollbarHeight, 16, this.field_148154_c - this.field_148153_b - 8);
            int scrollBarY = (int)this.field_148169_q * (this.field_148154_c - this.field_148153_b - scrollbarHeight) / k1 + this.field_148153_b;
            if (scrollBarY < this.field_148153_b) {
               scrollBarY = this.field_148153_b;
            }

            ++scrollBarY;
            --scrollbarHeight;
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
            worldrenderer.func_181662_b((double)scrollBarX, (double)(scrollBarY + scrollbarHeight), 0.0).func_187315_a(0.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
            worldrenderer.func_181662_b((double)l, (double)(scrollBarY + scrollbarHeight), 0.0).func_187315_a(1.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
            worldrenderer.func_181662_b((double)l, (double)scrollBarY, 0.0).func_187315_a(1.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
            worldrenderer.func_181662_b((double)scrollBarX, (double)scrollBarY, 0.0).func_187315_a(0.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
            tessellator.func_78381_a();
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
            worldrenderer.func_181662_b((double)scrollBarX, (double)(scrollBarY + scrollbarHeight - 1), 0.0).func_187315_a(0.0, 1.0).func_181669_b(192, 192, 192, 255).func_181675_d();
            worldrenderer.func_181662_b((double)(l - 1), (double)(scrollBarY + scrollbarHeight - 1), 0.0).func_187315_a(1.0, 1.0).func_181669_b(192, 192, 192, 255).func_181675_d();
            worldrenderer.func_181662_b((double)(l - 1), (double)scrollBarY, 0.0).func_187315_a(1.0, 0.0).func_181669_b(192, 192, 192, 255).func_181675_d();
            worldrenderer.func_181662_b((double)scrollBarX, (double)scrollBarY, 0.0).func_187315_a(0.0, 0.0).func_181669_b(192, 192, 192, 255).func_181675_d();
            tessellator.func_78381_a();
         }

         if (!this.func_148125_i()) {
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
            worldrenderer.func_181662_b((double)this.field_148152_e, (double)this.field_148154_c, 0.0).func_187315_a(0.0, 1.0).func_181669_b(0, 0, 0, 100).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148151_d, (double)this.field_148154_c, 0.0).func_187315_a(1.0, 1.0).func_181669_b(0, 0, 0, 100).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148151_d, (double)this.field_148153_b, 0.0).func_187315_a(1.0, 0.0).func_181669_b(0, 0, 0, 100).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_148152_e, (double)this.field_148153_b, 0.0).func_187315_a(0.0, 0.0).func_181669_b(0, 0, 0, 100).func_181675_d();
            tessellator.func_78381_a();
         }

         this.func_148142_b(mouseXIn, mouseYIn);
         GlStateManager.func_179098_w();
         GlStateManager.func_179103_j(7424);
         GlStateManager.func_179141_d();
         GlStateManager.func_179084_k();
         if (this.itemSlotContextMenu != null) {
            this.itemSlotContextMenu.drawScreen(mouseXIn, mouseYIn);
         }
      }

   }

   protected void func_192638_a(int p_148120_1_, int p_148120_2_, int mouseXIn, int mouseYIn, float partialTicks) {
      int i1 = this.func_148127_b();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder worldrenderer = tessellator.func_178180_c();

      for(int j1 = 0; j1 < i1; ++j1) {
         int k1 = p_148120_2_ + j1 * this.field_148149_f + this.field_148160_j;
         int l1 = this.field_148149_f - 4;
         if (k1 <= this.field_148154_c && k1 + l1 >= this.field_148153_b) {
            if (this.field_148166_t && this.func_148131_a(j1)) {
               int i2 = this.field_148152_e + (this.field_148155_a / 2 - this.func_148139_c() / 2);
               int j2 = this.field_148152_e + this.field_148155_a / 2 + this.func_148139_c() / 2;
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179090_x();
               worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
               worldrenderer.func_181662_b((double)i2, (double)(k1 + l1 + 2), 0.0).func_187315_a(0.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
               worldrenderer.func_181662_b((double)j2, (double)(k1 + l1 + 2), 0.0).func_187315_a(1.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
               worldrenderer.func_181662_b((double)j2, (double)(k1 - 2), 0.0).func_187315_a(1.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
               worldrenderer.func_181662_b((double)i2, (double)(k1 - 2), 0.0).func_187315_a(0.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
               worldrenderer.func_181662_b((double)(i2 + 1), (double)(k1 + l1 + 1), 0.0).func_187315_a(0.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
               worldrenderer.func_181662_b((double)(j2 - 1), (double)(k1 + l1 + 1), 0.0).func_187315_a(1.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
               worldrenderer.func_181662_b((double)(j2 - 1), (double)(k1 - 1), 0.0).func_187315_a(1.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
               worldrenderer.func_181662_b((double)(i2 + 1), (double)(k1 - 1), 0.0).func_187315_a(0.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
               tessellator.func_78381_a();
               GlStateManager.func_179098_w();
            }

            this.func_192637_a(j1, p_148120_1_, k1, l1, mouseXIn, mouseYIn, partialTicks);
         } else {
            this.func_192639_a(j1, p_148120_1_, k1, partialTicks);
         }
      }

   }
}

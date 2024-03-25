package com.pixelmonmod.pixelmon.client.gui.elements;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.input.Mouse;

public abstract class GuiSlotBase {
   public final int width;
   public final int height;
   public final int top;
   public final int bottom;
   public final int right;
   public final int left;
   protected int slotHeight;
   static final int DEFAULT_SLOT_HEIGHT = 10;
   int mouseX;
   int mouseY;
   protected float initialClickY = -2.0F;
   protected float scrollMultiplier;
   public int amountScrolled;
   protected int selectedElement = -1;
   protected long lastClicked = 0L;
   protected boolean opaque;

   public GuiSlotBase(int top, int left, int width, int height, boolean opaque) {
      this.width = width;
      this.height = height;
      this.top = top;
      this.bottom = this.top + this.height;
      this.slotHeight = 10;
      this.left = left;
      this.right = this.width + this.left;
      this.opaque = opaque;
   }

   protected abstract int getSize();

   protected abstract void elementClicked(int var1, boolean var2);

   protected abstract boolean isSelected(int var1);

   protected int getContentHeight() {
      return this.getSize() * this.slotHeight;
   }

   protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

   protected abstract float[] get1Color();

   protected int[] getSelectionColor() {
      return new int[]{0, 0, 0};
   }

   protected int[] get255Color() {
      float[] color1 = this.get1Color();
      return new int[]{(int)(color1[0] * 255.0F), (int)(color1[1] * 255.0F), (int)(color1[2] * 255.0F)};
   }

   public int func_27256_c(int par1, int par2) {
      if (!this.inBounds(par1, par2)) {
         return -1;
      } else {
         int var3 = this.left;
         int var4 = this.left + this.width;
         int var5 = par2 - this.top + this.amountScrolled - 4;
         int var6 = var5 / this.slotHeight;
         return par1 >= var3 && par1 <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize() ? var6 : -1;
      }
   }

   public boolean isMouseOver(int element, int par1, int par2) {
      return this.func_27256_c(par1, par2) == element;
   }

   public boolean isElementVisible(int i) {
      return this.getElementPosition(i) == 0;
   }

   public int getTopIndex() {
      return Math.max(0, this.amountScrolled / this.slotHeight);
   }

   public int getBottomIndex() {
      return Math.min(this.getTopIndex() + this.height / this.slotHeight - 1, this.getSize() - 1);
   }

   public int getElementPosition(int i) {
      int ti = this.getTopIndex();
      int bi = this.getBottomIndex();
      if (ti > i) {
         return -1;
      } else {
         return bi < i ? 1 : 0;
      }
   }

   public void scrollTo(int i) {
      int pos = this.getElementPosition(i);

      int prevAmountScrolled;
      while(pos > 0) {
         prevAmountScrolled = this.amountScrolled;
         this.amountScrolled += this.slotHeight;
         this.bindAmountScrolled();
         pos = this.getElementPosition(i);
         if (prevAmountScrolled == this.amountScrolled) {
            break;
         }
      }

      while(pos < 0) {
         prevAmountScrolled = this.amountScrolled;
         this.amountScrolled -= this.slotHeight;
         this.bindAmountScrolled();
         pos = this.getElementPosition(i);
         if (prevAmountScrolled == this.amountScrolled) {
            break;
         }
      }

   }

   public void bindAmountScrolled() {
      int var1 = this.getContentHeight() - (this.bottom - this.top - 4);
      if (var1 < 0) {
         var1 /= 2;
      }

      if (this.amountScrolled < 0) {
         this.amountScrolled = 0;
      }

      if (this.amountScrolled > var1) {
         this.amountScrolled = var1;
      }

   }

   boolean inBounds(int x, int y) {
      return x > this.left && x < this.right && y > this.top && y < this.bottom;
   }

   boolean inBoundsScroll(int x, int y) {
      return x > this.left && x < this.right + 6 && y > this.top && y < this.bottom;
   }

   public void drawScreen(int mousePosX, int mousePosY, float par3) {
      GlStateManager.func_179094_E();
      this.mouseX = mousePosX;
      this.mouseY = mousePosY;
      int length = this.getSize();
      int posScrollBar = this.left + this.width;
      int posScrollBar2 = posScrollBar + 6;
      GlStateManager.func_179097_i();
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179145_e();
      Minecraft mc = Minecraft.func_71410_x();
      mc.field_71460_t.func_78478_c();
      RenderHelper.func_74520_c();
      int scrollPosX;
      int scrollPosY;
      int index;
      int middleOfSelect;
      int topLeftOfSelect;
      if (Mouse.isButtonDown(0)) {
         if (this.initialClickY == -1.0F) {
            boolean flag = true;
            if (mousePosY >= this.top && mousePosY <= this.bottom) {
               int var8 = this.left;
               scrollPosX = this.right;
               scrollPosY = mousePosY - this.top + this.amountScrolled - 4;
               index = scrollPosY / this.slotHeight;
               if (this.inBounds(mousePosX, mousePosY) && index >= 0 && scrollPosY >= 0 && index < length) {
                  boolean var12 = index == this.selectedElement && System.currentTimeMillis() - this.lastClicked < 250L;
                  this.elementClicked(index, var12);
                  this.selectedElement = index;
                  this.lastClicked = System.currentTimeMillis();
               } else if (mousePosX >= var8 && mousePosX <= scrollPosX && scrollPosY < 0) {
                  flag = false;
               }

               if (mousePosX >= posScrollBar && mousePosX <= posScrollBar2) {
                  this.scrollMultiplier = -1.0F;
                  topLeftOfSelect = this.getContentHeight() - (this.bottom - this.top - 4);
                  if (topLeftOfSelect < 1) {
                     topLeftOfSelect = 1;
                  }

                  middleOfSelect = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
                  if (middleOfSelect < 32) {
                     middleOfSelect = 32;
                  }

                  if (middleOfSelect > this.bottom - this.top - 8) {
                     middleOfSelect = this.bottom - this.top - 8;
                  }

                  this.scrollMultiplier /= (float)(this.bottom - this.top - middleOfSelect) / (float)topLeftOfSelect;
               } else {
                  this.scrollMultiplier = 1.0F;
               }

               if (flag) {
                  this.initialClickY = (float)mousePosY;
               } else {
                  this.initialClickY = -2.0F;
               }
            } else {
               this.initialClickY = -2.0F;
            }
         } else if (this.initialClickY >= 0.0F && this.inBoundsScroll(mousePosX, mousePosY)) {
            this.amountScrolled = (int)((float)this.amountScrolled - ((float)mousePosY - this.initialClickY) * this.scrollMultiplier);
            this.initialClickY = (float)mousePosY;
         }
      } else if (this.inBoundsScroll(mousePosX, mousePosY)) {
         try {
            for(; !mc.field_71474_y.field_85185_A && Mouse.next(); mc.field_71462_r.func_146274_d()) {
               int scroll = Mouse.getEventDWheel();
               if (scroll != 0) {
                  if (scroll > 0) {
                     scroll = -1;
                  } else if (scroll < 0) {
                     scroll = 1;
                  }

                  this.amountScrolled += scroll * this.slotHeight;
               }
            }

            this.initialClickY = -1.0F;
         } catch (IOException var18) {
            var18.printStackTrace();
         }
      }

      this.bindAmountScrolled();
      GlStateManager.func_179140_f();
      GlStateManager.func_179106_n();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexBuffer = tessellator.func_178180_c();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      scrollPosX = this.left;
      scrollPosY = this.top + 4 - this.amountScrolled;
      this.drawBackground();

      for(index = 0; index < length; ++index) {
         topLeftOfSelect = scrollPosY + index * this.slotHeight;
         middleOfSelect = this.slotHeight - 4;
         if (topLeftOfSelect + 6 <= this.bottom && topLeftOfSelect + middleOfSelect - 8 >= this.top) {
            if (this.isHighlighted(index)) {
               this.drawSelection(middleOfSelect, topLeftOfSelect);
            }

            this.drawSlot(index, scrollPosX, topLeftOfSelect, middleOfSelect, tessellator);
         }
      }

      GlStateManager.func_179097_i();
      byte var20 = 4;
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179118_c();
      GlStateManager.func_179103_j(7425);
      GlStateManager.func_179090_x();
      int[] color = this.get255Color();
      if (color != null) {
         vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
         vertexBuffer.func_181662_b((double)this.left, (double)(this.top + var20), 0.0).func_187315_a(0.0, 1.0).func_181669_b(color[0], color[1], color[2], 0).func_181675_d();
         vertexBuffer.func_181662_b((double)this.right, (double)(this.top + var20), 0.0).func_187315_a(1.0, 1.0).func_181669_b(color[0], color[1], color[2], 0).func_181675_d();
         vertexBuffer.func_181662_b((double)this.right, (double)this.top, 0.0).func_187315_a(1.0, 0.0).func_181669_b(color[0], color[1], color[2], 255).func_181675_d();
         vertexBuffer.func_181662_b((double)this.left, (double)this.top, 0.0).func_187315_a(0.0, 0.0).func_181669_b(color[0], color[1], color[2], 255).func_181675_d();
         tessellator.func_78381_a();
         vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
         vertexBuffer.func_181662_b((double)this.left, (double)this.bottom + 2.0, 0.0).func_187315_a(0.0, 1.0).func_181669_b(color[0], color[1], color[2], 255).func_181675_d();
         vertexBuffer.func_181662_b((double)this.right, (double)this.bottom + 2.0, 0.0).func_187315_a(1.0, 1.0).func_181669_b(color[0], color[1], color[2], 255).func_181675_d();
         vertexBuffer.func_181662_b((double)this.right, (double)(this.bottom + 2 - var20), 0.0).func_187315_a(1.0, 0.0).func_181669_b(color[0], color[1], color[2], 0).func_181675_d();
         vertexBuffer.func_181662_b((double)this.left, (double)(this.bottom + 2 - var20), 0.0).func_187315_a(0.0, 0.0).func_181669_b(color[0], color[1], color[2], 0).func_181675_d();
         tessellator.func_78381_a();
      }

      topLeftOfSelect = this.getContentHeight() - (this.bottom - this.top - 4);
      if (topLeftOfSelect > 0) {
         middleOfSelect = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
         if (middleOfSelect < 32) {
            middleOfSelect = 32;
         }

         if (middleOfSelect > this.bottom - this.top - 8) {
            middleOfSelect = this.bottom - this.top - 8;
         }

         int leftish = this.amountScrolled * (this.bottom - this.top - middleOfSelect) / topLeftOfSelect + this.top;
         if (leftish < this.top) {
            leftish = this.top;
         }

         vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
         vertexBuffer.func_181662_b((double)posScrollBar, (double)this.bottom + 2.0, 0.0).func_187315_a(0.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)posScrollBar2, (double)this.bottom + 2.0, 0.0).func_187315_a(1.0, 1.0).func_181669_b(0, 0, 0, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)posScrollBar2, (double)this.top + 2.0, 0.0).func_187315_a(1.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)posScrollBar, (double)this.top + 2.0, 0.0).func_187315_a(0.0, 0.0).func_181669_b(0, 0, 0, 255).func_181675_d();
         tessellator.func_78381_a();
         vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
         vertexBuffer.func_181662_b((double)posScrollBar, (double)(leftish + middleOfSelect + 2), 0.0).func_187315_a(0.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)posScrollBar2, (double)(leftish + middleOfSelect + 2), 0.0).func_187315_a(1.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)posScrollBar2, (double)leftish + 2.0, 0.0).func_187315_a(1.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)posScrollBar, (double)leftish + 2.0, 0.0).func_187315_a(0.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
         tessellator.func_78381_a();
         vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
         vertexBuffer.func_181662_b((double)posScrollBar, (double)(leftish + middleOfSelect - 1 + 2), 0.0).func_187315_a(0.0, 1.0).func_181669_b(192, 192, 192, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)(posScrollBar2 - 1), (double)(leftish + middleOfSelect - 1 + 2), 0.0).func_187315_a(1.0, 1.0).func_181669_b(192, 192, 192, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)(posScrollBar2 - 1), (double)leftish + 2.0, 0.0).func_187315_a(1.0, 0.0).func_181669_b(192, 192, 192, 255).func_181675_d();
         vertexBuffer.func_181662_b((double)posScrollBar, (double)leftish + 2.0, 0.0).func_187315_a(0.0, 0.0).func_181669_b(192, 192, 192, 255).func_181675_d();
         tessellator.func_78381_a();
      }

      GlStateManager.func_179098_w();
      GlStateManager.func_179145_e();
      GlStateManager.func_179103_j(7424);
      GlStateManager.func_179141_d();
      GlStateManager.func_179084_k();
      GlStateManager.func_179121_F();
   }

   boolean hasScrollBar() {
      return this.getContentHeight() - (this.bottom - this.top - 4) > 0;
   }

   protected void drawBackground() {
   }

   boolean isHighlighted(int index) {
      return this.isSelected(index) || this.isMouseOver(index, this.mouseX, this.mouseY);
   }

   void drawSelection(int middleOfSelect, int topLeftOfSelect) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexBuffer = tessellator.func_178180_c();
      int leftish = this.left;
      int rightish = this.left + this.width;
      float[] color = this.get1Color();
      GlStateManager.func_179131_c(color[0], color[1], color[2], 255.0F);
      GlStateManager.func_179090_x();
      VertexFormat vertexFormat = this.opaque ? DefaultVertexFormats.field_181709_i : DefaultVertexFormats.field_181707_g;
      vertexBuffer.func_181668_a(7, vertexFormat);
      if (this.opaque) {
         vertexBuffer.func_181662_b(80.0, (double)(topLeftOfSelect + middleOfSelect + 2), 0.0).func_187315_a(0.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
         vertexBuffer.func_181662_b(80.0, (double)(topLeftOfSelect + middleOfSelect + 2), 0.0).func_187315_a(1.0, 1.0).func_181669_b(128, 128, 128, 255).func_181675_d();
         vertexBuffer.func_181662_b(80.0, (double)(topLeftOfSelect - 2), 0.0).func_187315_a(1.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
         vertexBuffer.func_181662_b(80.0, (double)(topLeftOfSelect - 2), 0.0).func_187315_a(0.0, 0.0).func_181669_b(128, 128, 128, 255).func_181675_d();
      } else {
         vertexBuffer.func_181662_b(80.0, (double)(topLeftOfSelect + middleOfSelect + 2), 0.0).func_187315_a(0.0, 1.0).func_181675_d();
         vertexBuffer.func_181662_b(80.0, (double)(topLeftOfSelect + middleOfSelect + 2), 0.0).func_187315_a(1.0, 1.0).func_181675_d();
         vertexBuffer.func_181662_b(80.0, (double)(topLeftOfSelect - 2), 0.0).func_187315_a(1.0, 0.0).func_181675_d();
         vertexBuffer.func_181662_b(80.0, (double)(topLeftOfSelect - 2), 0.0).func_187315_a(0.0, 0.0).func_181675_d();
      }

      if (this.opaque) {
         int[] selectionColor = this.getSelectionColor();
         if (selectionColor != null) {
            vertexBuffer.func_181662_b((double)(leftish + 1), (double)(topLeftOfSelect + middleOfSelect + 1), 0.0).func_187315_a(0.0, 1.0).func_181669_b(selectionColor[0], selectionColor[1], selectionColor[2], 255).func_181675_d();
            vertexBuffer.func_181662_b((double)(rightish - 1), (double)(topLeftOfSelect + middleOfSelect + 1), 0.0).func_187315_a(1.0, 1.0).func_181669_b(selectionColor[0], selectionColor[1], selectionColor[2], 255).func_181675_d();
            vertexBuffer.func_181662_b((double)(rightish - 1), (double)(topLeftOfSelect - 1), 0.0).func_187315_a(1.0, 0.0).func_181669_b(selectionColor[0], selectionColor[1], selectionColor[2], 255).func_181675_d();
            vertexBuffer.func_181662_b((double)(leftish + 1), (double)(topLeftOfSelect - 1), 0.0).func_187315_a(0.0, 0.0).func_181669_b(selectionColor[0], selectionColor[1], selectionColor[2], 255).func_181675_d();
         }
      } else {
         vertexBuffer.func_181662_b((double)(leftish + 1), (double)(topLeftOfSelect + middleOfSelect + 1), 0.0).func_187315_a(0.0, 1.0).func_181675_d();
         vertexBuffer.func_181662_b((double)(rightish - 1), (double)(topLeftOfSelect + middleOfSelect + 1), 0.0).func_187315_a(1.0, 1.0).func_181675_d();
         vertexBuffer.func_181662_b((double)(rightish - 1), (double)(topLeftOfSelect - 1), 0.0).func_187315_a(1.0, 0.0).func_181675_d();
         vertexBuffer.func_181662_b((double)(leftish + 1), (double)(topLeftOfSelect - 1), 0.0).func_187315_a(0.0, 0.0).func_181675_d();
      }

      tessellator.func_78381_a();
      GlStateManager.func_179098_w();
   }

   public int getMouseOverIndex(int mouseX, int mouseY) {
      for(int i = this.getTopIndex(); i <= this.getBottomIndex(); ++i) {
         if (this.isMouseOver(i, mouseX, mouseY)) {
            return i;
         }
      }

      return -1;
   }

   public int getCenterX() {
      return this.left + this.width / 2;
   }
}

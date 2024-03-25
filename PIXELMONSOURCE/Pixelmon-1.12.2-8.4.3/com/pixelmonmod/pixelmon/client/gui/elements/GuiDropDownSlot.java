package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.input.Mouse;

class GuiDropDownSlot extends GuiSlotBase {
   private GuiDropDown dropDown;
   private int dropDownWidth;

   public GuiDropDownSlot(GuiDropDown dropDown, int top, int left, int width, int height) {
      super(top, left, width, height, true);
      this.dropDown = dropDown;
      this.dropDownWidth = width;
   }

   public GuiDropDownSlot(GuiDropDown dropDown, int top, int left, int width, int height, int dropDownWidth) {
      super(top, left, width, height, true);
      this.dropDown = dropDown;
      this.dropDownWidth = dropDownWidth;
   }

   protected int getSize() {
      return this.dropDown.options.size();
   }

   protected void elementClicked(int index, boolean doubleClicked) {
      this.dropDown.elementClicked(index);
   }

   protected boolean isSelected(int element) {
      return element == this.dropDown.selectedIndex;
   }

   protected void drawSlot(int index, int x, int yTop, int yMiddle, Tessellator tessellator) {
      this.drawOptionString(index, yTop);
   }

   void drawOptionString(int index, int y) {
      if (index >= 0 && index < this.dropDown.options.size()) {
         this.drawOptionString(this.dropDown.options.get(index), y);
      }
   }

   void drawOptionString(Object option, int y) {
      if (option != null) {
         int width = this.dropDown.active ? this.dropDownWidth : this.width;
         String optionString = this.dropDown.toOptionString(option);
         String limitedString = GuiHelper.getLimitedString(optionString, width);
         int color = 0;
         switch (this.dropDown.align) {
            case Left:
               FontRenderer fr = Minecraft.func_71410_x().field_71466_p;
               GuiHelper.drawSquashedString(fr, optionString, fr.field_78293_l, (double)(width - 3), (float)(this.left + 2), (float)y, color, false);
               break;
            case Center:
               GuiHelper.drawCenteredString(limitedString, (float)this.left + (float)width / 2.0F, (float)y, color);
               break;
            case Right:
               GuiHelper.drawStringRightAligned(limitedString, (float)(this.left + width), (float)y, color);
         }

      }
   }

   protected void drawBackground() {
      this.drawBackgroundRect(this.top + 3, this.top + this.height, -5000269);
   }

   void drawBackgroundRect(int top, int bottom, int color) {
      Gui.func_73734_a(this.left, top, this.left + (this.dropDown.active ? this.dropDownWidth : this.width), bottom, color);
   }

   protected float[] get1Color() {
      return new float[]{1.0F, 1.0F, 1.0F};
   }

   protected int[] getSelectionColor() {
      return new int[]{128, 128, 128};
   }

   protected int[] get255Color() {
      return null;
   }

   public int func_27256_c(int par1, int par2) {
      if (this.inBounds(par1, par2)) {
         int var3 = this.left;
         int var4 = this.left + (this.dropDown.active ? this.dropDownWidth : this.width);
         int var5 = par2 - this.top + this.amountScrolled - 4;
         int var6 = var5 / this.slotHeight;
         return par1 >= var3 && par1 <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize() ? var6 : -1;
      } else {
         return -1;
      }
   }

   boolean inBounds(int x, int y) {
      int right = (this.dropDown.active ? this.dropDownWidth : this.width) + this.left;
      return x > this.left && x < right && y > this.top && y < this.bottom;
   }

   boolean inBoundsScroll(int x, int y) {
      int right = (this.dropDown.active ? this.dropDownWidth : this.width) + this.left;
      return x > this.left && x < right + 6 && y > this.top && y < this.bottom;
   }

   void drawSelection(int middleOfSelect, int topLeftOfSelect) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexBuffer = tessellator.func_178180_c();
      int leftish = this.left;
      int rightish = this.left + (this.dropDown.active ? this.dropDownWidth : this.width);
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

   public void drawScreen(int mousePosX, int mousePosY, float par3) {
      GlStateManager.func_179094_E();
      this.mouseX = mousePosX;
      this.mouseY = mousePosY;
      int width = this.dropDown.active ? this.dropDownWidth : this.width;
      int length = this.getSize();
      int posScrollBar = this.left + width;
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
         } catch (IOException var19) {
            var19.printStackTrace();
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
}

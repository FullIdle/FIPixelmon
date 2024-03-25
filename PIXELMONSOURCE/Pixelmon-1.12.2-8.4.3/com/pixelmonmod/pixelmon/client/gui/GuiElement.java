package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.client.gui.battles.pokemonOverlays.AllyElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public abstract class GuiElement {
   protected int x;
   protected int y;
   protected int width;
   protected int height;
   protected float zLevel;
   protected float scale;

   public abstract void drawElement(float var1);

   public GuiElement setPosition(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.scale = 1.0F;
      return this;
   }

   public boolean isMouseOver(int mouseX, int mouseY) {
      return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
   }

   public void drawElementScaled(int x, int y, int width, int height, float scale, int index, GuiScreen parent, GuiParticleEngine engine) {
      this.scale = scale;
      boolean ally = this instanceof AllyElement;
      this.setPosition(x, y, width, height);
      GlStateManager.func_179094_E();
      double tX = (double)x;
      double tY = (double)y;
      if (ally) {
         tX = (double)parent.field_146294_l;
         tY = (double)((float)(y + height) + 2.0F / scale);
         GlStateManager.func_179137_b(tX, tY, 0.0);
      }

      GlStateManager.func_179152_a(scale, scale, scale);
      if (ally) {
         GlStateManager.func_179137_b(-tX, -tY, 0.0);
      }

      this.drawElement(scale);
      GlStateManager.func_179121_F();
      if (ally) {
         this.setPosition((int)((double)parent.field_146294_l - 190.0 * (double)scale * (double)(index + 1)) + 20, (int)((float)parent.field_146295_m - 140.0F * scale), (int)((float)width * scale), (int)((float)height * scale));
      } else {
         this.setPosition((int)((float)x * scale), (int)((float)y * scale), (int)((float)width * scale), (int)((float)height * scale));
      }

   }
}

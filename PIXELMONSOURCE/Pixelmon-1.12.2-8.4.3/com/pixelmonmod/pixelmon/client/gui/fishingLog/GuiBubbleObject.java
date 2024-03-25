package com.pixelmonmod.pixelmon.client.gui.fishingLog;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiBubbleObject {
   private float x;
   private float y;
   private float size;
   private float alpha = RandomHelper.getRandomNumberBetween(0.6F, 1.0F);

   public GuiBubbleObject(float x, float y, float size) {
      this.x = x;
      this.y = y;
      this.size = size;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }

   public float getSize() {
      return this.size;
   }

   public void setSize(float size) {
      this.size = size;
   }

   public void render() {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(GuiResources.fishingLogBubble);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, this.alpha);
      this.y -= this.size / 10.0F;
      GuiHelper.drawImageQuad((double)this.x, (double)this.y, (double)this.size, this.size, 0.0, 0.0, 1.0, 1.0, 0.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
   }
}

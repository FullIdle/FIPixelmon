package com.pixelmonmod.tcg.gui;

import net.minecraft.util.ResourceLocation;

public class GuiTarget {
   protected int x;
   protected int y;
   protected int w;
   protected int h;
   protected float z;
   protected static ResourceLocation emptyTexture = new ResourceLocation("tcg:/gui/cards/card_empty_bold.png");

   public GuiTarget(float z) {
      this.z = z;
   }

   public GuiTarget(int x, int y, int w, int h, float z) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.z = z;
   }

   public void draw(int mouseX, int mouseY) {
   }

   public boolean isInside(int mouseX, int mouseY) {
      return isClamp(mouseX, this.x, this.x + this.w) && isClamp(mouseY, this.y, this.y + this.h);
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getWidth() {
      return this.w;
   }

   public void setWidth(int w) {
      this.w = w;
   }

   public int getHeight() {
      return this.h;
   }

   public void setHeight(int h) {
      this.h = h;
   }

   private static boolean isClamp(int value, int bound1, int bound2) {
      if (bound2 > bound1) {
         return value <= bound2 && value >= bound1;
      } else {
         return value <= bound1 && value >= bound2;
      }
   }
}

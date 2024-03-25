package com.pixelmonmod.pixelmon.quests.client.editor.ui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiSimpleButton extends Gui {
   public final int id;
   public int x;
   public int y;
   public int w;
   public int h;
   public int bX;
   public int bY;
   private String text;
   private ResourceLocation image;
   private boolean isEnabled;
   private int enabledColor;
   private int disabledColor;

   public GuiSimpleButton(int id, int x, int y, int size, String text, int color) {
      this(id, x, y, size, size, text, color);
   }

   public GuiSimpleButton(int id, int x, int y, int size, ResourceLocation image, int bX, int bY, int color) {
      this(id, x, y, size, size, image, bX, bY, color);
   }

   public GuiSimpleButton(int id, int x, int y, int w, int h, String text, int color) {
      this(id, x, y, w, h, color);
      this.text = text;
   }

   public GuiSimpleButton(int id, int x, int y, int w, int h, ResourceLocation image, int bX, int bY, int color) {
      this(id, x, y, w, h, color);
      this.image = image;
      this.bX = bX;
      this.bY = bY;
   }

   private GuiSimpleButton(int id, int x, int y, int size, int color) {
      this(id, x, y, size, size, color);
   }

   private GuiSimpleButton(int id, int x, int y, int w, int h, int color) {
      this.text = null;
      this.image = null;
      this.isEnabled = true;
      this.disabledColor = -9079435;
      this.id = id;
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.enabledColor = color;
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   public void setEnabled(boolean isEnabled) {
      this.isEnabled = isEnabled;
   }

   public void draw() {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179141_d();
      GlStateManager.func_179112_b(770, 771);
      func_73734_a(this.x, this.y, this.x + this.w, this.y + this.h, this.isEnabled ? this.enabledColor : this.disabledColor);
      if (this.image != null) {
         Minecraft.func_71410_x().field_71446_o.func_110577_a(this.image);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.8F);
         Gui.func_146110_a(this.x + this.bX, this.y + this.bY, 0.0F, 0.0F, this.w - this.bX * 2, this.h - this.bY * 2, (float)(this.w - this.bX * 2), (float)(this.h - this.bY * 2));
      } else if (this.text != null) {
         GuiHelper.drawCenteredString(this.text, (float)this.x + (float)this.w / 2.0F, (float)(this.y - 4) + (float)this.h / 2.0F, 16777215, true);
      }

      GlStateManager.func_179118_c();
      GlStateManager.func_179084_k();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean isWithin(int x, int y) {
      return this.x <= x && this.x + this.w >= x && this.y <= y && this.y + this.h >= y;
   }
}

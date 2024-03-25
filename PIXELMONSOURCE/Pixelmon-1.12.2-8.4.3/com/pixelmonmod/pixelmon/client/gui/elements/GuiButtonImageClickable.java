package com.pixelmonmod.pixelmon.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImageClickable extends GuiButton {
   private ResourceLocation resource;
   private int textureX;
   private int textureY;
   private boolean pressed = false;

   public GuiButtonImageClickable(int buttonId, int x, int y, int textureX, int textureY, int width, int height) {
      super(buttonId, x, y, width, height, "");
      this.textureX = textureX;
      this.textureY = textureY;
   }

   public GuiButtonImageClickable updatePosition(int x, int y) {
      this.field_146128_h = x;
      this.field_146129_i = y;
      return this;
   }

   public void setText(String text) {
      this.field_146126_j = text;
   }

   public void setImage(ResourceLocation resource) {
      this.resource = resource;
   }

   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
      return this.pressed = super.func_146116_c(mc, mouseX, mouseY);
   }

   public void func_146118_a(int mouseX, int mouseY) {
      this.pressed = false;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.field_146125_m) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         mc.func_110434_K().func_110577_a(this.resource);
         this.func_73729_b(this.field_146128_h, this.field_146129_i + this.field_146121_g - 2, this.textureX, this.textureY + this.field_146121_g, this.field_146120_f, 4);
         this.func_73729_b(this.field_146128_h, this.field_146129_i + (this.pressed ? 1 : 0), this.textureX, this.textureY, this.field_146120_f, this.field_146121_g);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.func_73732_a(mc.field_71466_p, this.field_146126_j, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + (this.field_146121_g - (this.pressed ? 7 : 8)) / 2, 16777215);
      }

   }
}

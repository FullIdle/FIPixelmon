package com.pixelmonmod.pixelmon.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiInvisibleButton extends GuiButton {
   public GuiInvisibleButton(int buttonId, GuiScreen parent) {
      super(buttonId, 0, 0, "");
      this.setPosition(0, 0, parent.field_146294_l, parent.field_146295_m);
   }

   public GuiButton setPosition(int x, int y, int width, int height) {
      this.field_146128_h = x;
      this.field_146129_i = y;
      this.field_146120_f = width;
      this.field_146121_g = height;
      return this;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.field_146125_m) {
      }

   }

   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
      return mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
   }

   public void func_146118_a(int mouseX, int mouseY) {
   }
}

package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.client.render.custom.FontRendererPixelmon;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.resources.I18n;

public class GuiPixelLabel extends GuiLabel {
   public GuiPixelLabel(String text, int id, int x, int y, int width, int height, int textColor) {
      super(FontRendererPixelmon.getInstance(), id, x, y, width, height, textColor);
      this.func_175202_a(I18n.func_135052_a(text, new Object[0]));
      this.func_175203_a();
   }
}

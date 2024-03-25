package com.pixelmonmod.pixelmon.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class FontScaler {
   public static void scaleFont(FontRenderer f, String s, int x, int y, int color, double scale) {
      GlStateManager.func_179139_a(scale, scale, scale);
      f.func_78276_b(s, x, y, color);
      double fixScale = 1.0 / scale;
      GlStateManager.func_179139_a(fixScale, fixScale, fixScale);
   }
}

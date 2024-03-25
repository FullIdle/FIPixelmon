package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.gui.base.TCGGuiScreen;
import com.pixelmonmod.tcg.helper.CardHelper;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;

public class GuiGuideBeginner extends TCGGuiScreen {
   public static final int TEXTURE_WIDTH = 2480;
   public static final int TEXTURE_HEIGHT = 1748;

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      CardHelper.resetColour(1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/guides/beginner/page.png"));
      float scale = 3.6F;
      GuiHelper.drawImageQuad((double)((float)(this.field_146294_l / 2) - 620.0F / scale), (double)((float)(this.field_146295_m / 2) - 437.0F / scale), (double)(1240.0F / scale), 874.0F / scale, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      CardHelper.resetColour(1.0F);
   }

   public boolean func_73868_f() {
      return true;
   }
}

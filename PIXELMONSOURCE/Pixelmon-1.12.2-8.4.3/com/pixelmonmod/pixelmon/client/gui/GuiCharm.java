package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.items.EnumCharms;
import com.pixelmonmod.pixelmon.storage.ClientData;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.util.vector.Vector4f;

public class GuiCharm extends GuiScreen {
   private final EnumCharms charm;

   public GuiCharm(EnumCharms charm) {
      this.charm = charm;
   }

   public void func_73863_a(int i, int j, float f) {
      GuiHelper.drawGradientRect(0, 0, this.field_73735_i, this.field_146294_l, this.field_146295_m, new Vector4f(0.0F, 0.0F, 0.0F, 0.4F), new Vector4f(0.0F, 0.0F, 0.0F, 0.4F), true);
      String s = I18n.func_135052_a(this.charm.getGuiMessage(), new Object[0]);
      this.func_73732_a(this.field_146297_k.field_71466_p, s, this.field_146294_l / 2, this.field_146295_m / 2 - 75, 16777215);
      int buttonSize = 70;
      this.field_146297_k.field_71446_o.func_110577_a(this.charm.getImage());
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      int buttonLeft = this.field_146294_l / 2 - buttonSize / 2 - 40;
      int buttonTop = this.field_146295_m / 2 - buttonSize / 2 - 10;
      if (i >= buttonLeft && i <= buttonLeft + buttonSize && j >= buttonTop && j <= buttonTop + buttonSize) {
         GuiHelper.drawGradientRect(buttonLeft, buttonTop, this.field_73735_i, buttonLeft + buttonSize, buttonTop + buttonSize, new Vector4f(0.8F, 0.8F, 0.8F, 1.0F), new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), true);
      } else {
         GuiHelper.drawGradientRect(buttonLeft, buttonTop, this.field_73735_i, buttonLeft + buttonSize, buttonTop + buttonSize, new Vector4f(0.2F, 0.2F, 0.2F, 1.0F), new Vector4f(0.0F, 0.0F, 0.0F, 1.0F), true);
      }

      GuiHelper.drawImageQuad((double)(buttonLeft + 3), (double)(buttonTop + 3), (double)(buttonSize - 6), (float)(buttonSize - 6), 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.noItem);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      buttonLeft = this.field_146294_l / 2 - buttonSize / 2 + 40;
      if (i >= buttonLeft && i <= buttonLeft + buttonSize && j >= buttonTop && j <= buttonTop + buttonSize) {
         GuiHelper.drawGradientRect(buttonLeft, buttonTop, this.field_73735_i, buttonLeft + buttonSize, buttonTop + buttonSize, new Vector4f(0.8F, 0.8F, 0.8F, 1.0F), new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), true);
      } else {
         GuiHelper.drawGradientRect(buttonLeft, buttonTop, this.field_73735_i, buttonLeft + buttonSize, buttonTop + buttonSize, new Vector4f(0.2F, 0.2F, 0.2F, 1.0F), new Vector4f(0.0F, 0.0F, 0.0F, 1.0F), true);
      }

      GuiHelper.drawGradientRect(buttonLeft + 3, buttonTop + 3, this.field_73735_i, buttonLeft + buttonSize - 6, buttonTop + buttonSize - 6, new Vector4f(0.8F, 0.8F, 0.8F, 1.0F), new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), true);
      GuiHelper.drawImageQuad((double)(buttonLeft + 3), (double)(buttonTop + 3), (double)(buttonSize - 6), (float)(buttonSize - 6), 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      int buttonSize = 70;
      int buttonLeft = this.field_146294_l / 2 - buttonSize / 2 - 40;
      int buttonTop = this.field_146295_m / 2 - buttonSize / 2 - 10;
      if (mouseX >= buttonLeft && mouseX <= buttonLeft + buttonSize && mouseY >= buttonTop && mouseY <= buttonTop + buttonSize) {
         this.charm.sendPacket(EnumFeatureState.Active);
         this.field_146297_k.field_71439_g.func_71053_j();
      } else {
         buttonLeft = this.field_146294_l / 2 - buttonSize / 2 + 40;
         if (mouseX >= buttonLeft && mouseX <= buttonLeft + buttonSize && mouseY >= buttonTop && mouseY <= buttonTop + buttonSize) {
            this.charm.sendPacket(EnumFeatureState.Available);
            this.field_146297_k.field_71439_g.func_71053_j();
         }

      }
   }

   public void func_146281_b() {
      ClientData.openMegaItemGui = -1;
      super.func_146281_b();
   }
}

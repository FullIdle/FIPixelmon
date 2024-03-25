package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.client.SoundHelper;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

public abstract class GuiWarning extends GuiScreen {
   protected int guiLeft;
   protected int guiTop;
   private boolean overY = false;
   private boolean overN = false;
   protected GuiScreen previousScreen;

   protected GuiWarning(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.guiLeft = (this.field_146294_l - 176) / 2;
      this.guiTop = (this.field_146295_m - 166) / 2;
      this.field_146292_n.clear();
   }

   protected void func_73864_a(int mouseX, int mouseY, int par3) throws IOException {
      super.func_73864_a(mouseX, mouseY, par3);
      if (this.overY) {
         this.confirmAction();
         this.pressButton();
      } else if (this.overN) {
         this.pressButton();
      }

   }

   protected abstract void confirmAction();

   private void pressButton() {
      SoundHelper.playButtonPressSound();
      this.closeScreen();
   }

   private void closeScreen() {
      this.field_146297_k.func_147108_a(this.previousScreen);
   }

   protected abstract void drawWarningText();

   public void func_73863_a(int mouseX, int mouseY, float f) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.yesNo);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 128), (double)(this.field_146295_m / 2 - 50), 256.0, 100.0F, 0.0, 0.0, 1.0, 0.78125, this.field_73735_i);
      this.overY = mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 - 33 && mouseY < this.field_146295_m / 2 - 7;
      this.overN = mouseX > this.field_146294_l / 2 + 63 && mouseX < this.field_146294_l / 2 + 108 && mouseY > this.field_146295_m / 2 + 5 && mouseY < this.field_146295_m / 2 + 31;
      int yesColor;
      if (this.overY) {
         yesColor = 16777120;
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 63), (double)(this.field_146295_m / 2 - 33), 45.0, 26.0F, 0.6015625, 0.7890625, 0.77734375, 0.9921875, this.field_73735_i);
      } else {
         yesColor = 16777215;
      }

      int noColor;
      if (this.overN) {
         noColor = 16777120;
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 63), (double)(this.field_146295_m / 2 + 5), 45.0, 26.0F, 0.6015625, 0.7890625, 0.77734375, 0.9921875, this.field_73735_i);
      } else {
         noColor = 16777215;
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.yesno.yes", new Object[0]), this.field_146294_l / 2 + 76, this.field_146295_m / 2 - 23, yesColor);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.yesno.no", new Object[0]), this.field_146294_l / 2 + 80, this.field_146295_m / 2 + 15, noColor);
      int i = this.guiLeft;
      int j = this.guiTop;
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)i, (float)j, 0.0F);
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      this.drawWarningText();
      GlStateManager.func_179121_F();
   }

   protected void drawCenteredSplitText(String text) {
      GuiHelper.drawCenteredSplitString(text, 60.0F, 78.0F, 200, 16777215);
   }
}

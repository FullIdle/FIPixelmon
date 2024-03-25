package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.yesNo;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleScreen;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public abstract class YesNoDialogue extends BattleScreen {
   public YesNoDialogue(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.yesNo);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(width / 2 - 128), (double)(height / 2 - 50), 256.0, 100.0F, 0.0, 0.0, 1.0, 0.78125, this.field_73735_i);
      this.drawConfirmText(width, height);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.yesNo);
      if (mouseX > width / 2 + 63 && mouseX < width / 2 + 108 && mouseY > height / 2 - 33 && mouseY < height / 2 - 7) {
         GuiHelper.drawImageQuad((double)(width / 2 + 63), (double)(height / 2 - 33), 45.0, 26.0F, 0.6015625, 0.7890625, 0.77734375, 0.9921875, this.field_73735_i);
      }

      if (mouseX > width / 2 + 63 && mouseX < width / 2 + 108 && mouseY > height / 2 + 5 && mouseY < height / 2 + 31) {
         GuiHelper.drawImageQuad((double)(width / 2 + 63), (double)(height / 2 + 5), 45.0, 26.0F, 0.6015625, 0.7890625, 0.77734375, 0.9921875, this.field_73735_i);
      }

      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.yesno.yes", new Object[0]), width / 2 + 76, height / 2 - 23, 16777215);
      this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.yesno.no", new Object[0]), width / 2 + 80, height / 2 + 15, 16777215);
   }

   protected abstract void drawConfirmText(int var1, int var2);

   public void click(int width, int height, int mouseX, int mouseY) {
      if (mouseX > width / 2 + 63 && mouseX < width / 2 + 108) {
         if (mouseY > height / 2 - 33 && mouseY < height / 2 - 7) {
            this.confirm();
         } else if (mouseY > height / 2 + 5 && mouseY < height / 2 + 31) {
            this.bm.mode = this.bm.oldMode;
         }
      }

   }

   protected abstract void confirm();
}

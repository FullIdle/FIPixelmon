package com.pixelmonmod.pixelmon.client.gui.battles.rules;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiBattleRulesFixed extends GuiBattleRulesBase {
   private GuiButton okayButton;

   public GuiBattleRulesFixed() {
      this.editingEnabled = false;
      this.rules = ClientProxy.battleManager.rules;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      int buttonWidth = 30;
      this.okayButton = new GuiButton(0, this.centerX - buttonWidth / 2, this.centerY + 60, buttonWidth, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0]));
      this.field_146292_n.add(this.okayButton);
   }

   protected void func_73869_a(char key, int keyCode) {
   }

   protected void drawBackgroundUnderMenus(float partialTicks, int mouseX, int mouseY) {
      super.drawBackgroundUnderMenus(partialTicks, mouseX, mouseY);
      this.dimScreen();
      this.highlightButtons(40, 67);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button == this.okayButton) {
         if (GuiTeamSelect.teamSelectPacket.isAllDisabled()) {
            this.field_146297_k.field_71439_g.func_145747_a(new TextComponentTranslation("gui.battlerules.cancelselectyou", new Object[0]));
            GuiHelper.closeScreen();
         } else {
            this.field_146297_k.func_147108_a(new GuiTeamSelect());
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}

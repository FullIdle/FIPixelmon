package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.util.helpers.CursorHelper;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;

public abstract class BattleMenuGui extends BattleGui {
   protected BattleMenuElement menuElement = new BattleMenuElement(this);

   public BattleMenuGui(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public abstract String getTitle();

   public abstract List generateButtons();

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      CursorHelper.setCursor(CursorHelper.DEFAULT_CURSOR);
      this.drawBackground(width, height, mouseX, mouseY);
      this.drawButtons(mouseX, mouseY);

      try {
         this.parent.battleLog.drawElement(40, height - 80, Math.min(this.field_146294_l - 80, 260), 80, false);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, 1879048192, 1879048192);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.menuElement.setTitle(this.getTitle());
      this.menuElement.setButtons(this.generateButtons());
      this.menuElement.setPosition(0, this.field_146295_m - 240, 180, 240);
      this.menuElement.drawElement(1.0F);
   }
}

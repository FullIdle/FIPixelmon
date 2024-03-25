package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens;

import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleGui;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import com.pixelmonmod.pixelmon.util.helpers.CursorHelper;
import net.minecraft.client.resources.I18n;

public class WaitingScreen extends BattleGui {
   private int flashCounter = 0;

   public WaitingScreen(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      CursorHelper.setCursor(CursorHelper.DEFAULT_CURSOR);
      this.drawBackground(width, height, mouseX, mouseY);
      this.drawButtons(mouseX, mouseY);
      if (!this.bm.hasMoreMessages()) {
         String ellipses = "";
         if (this.flashCounter < 20) {
            ellipses = ".";
         } else if (this.flashCounter < 40) {
            ellipses = "..";
         } else if (this.flashCounter < 60) {
            ellipses = "...";
         }

         this.parent.battleLog.setActiveMessage(I18n.func_135052_a("gui.guiDoctor.waiting", new Object[0]) + ellipses);
      } else {
         this.parent.battleLog.setActiveMessage("");
      }

      try {
         this.parent.battleLog.drawElement(40, height - 80, Math.min(this.field_146294_l - 80, 260), 80, true);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void func_73876_c() {
      if (this.bm.mode == BattleMode.Waiting && !this.bm.hasMoreMessages()) {
         if (++this.flashCounter >= 60) {
            this.flashCounter = 0;
         }
      } else {
         this.flashCounter = 0;
      }

      this.parent.battleLog.func_73660_a();
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (!this.parent.battleLog.processUpDown(mouseX, mouseY)) {
         this.parent.battleLog.acknowledge();
         this.flashCounter = 0;
      }
   }

   public boolean isScreen() {
      return super.isScreen() || this.bm.hasMoreMessages() && this.bm.mode == BattleMode.MainMenu;
   }
}

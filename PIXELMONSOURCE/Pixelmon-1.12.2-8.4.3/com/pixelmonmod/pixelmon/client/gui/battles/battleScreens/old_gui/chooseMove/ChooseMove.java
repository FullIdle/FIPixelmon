package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.old_gui.chooseMove;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleScreen;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public abstract class ChooseMove extends BattleScreen {
   public ChooseMove(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.chooseMove2);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(width / 2 - 128), (double)(height / 2 - 102), 256.0, 205.0F, 0.0, 0.0, 1.0, 0.80078125, this.field_73735_i);
      if (this.mouseOverBack(width, height, mouseX, mouseY)) {
         GuiHelper.drawImageQuad((double)(width / 2 + 109), (double)(height / 2 + 89), 12.0, 10.0F, 0.3125, 0.8171206116676331, 0.359375, 0.85546875, this.field_73735_i);
      }

      PixelmonInGui pixelmon = this.bm.getCurrentPokemon();
      if (pixelmon == null) {
         GuiHelper.closeScreen();
      } else {
         GuiHelper.drawMoveset(pixelmon.moveset, width, height, this.field_73735_i);

         for(int i = 0; i < pixelmon.moveset.size(); ++i) {
            if (this.mouseOverMove(i, width, height, mouseX, mouseY)) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.chooseMove2);
               GuiHelper.drawImageQuad((double)(width / 2 - 30), (double)(height / 2 - 94 + 22 * i), 152.0, 24.0F, 0.37890625, 0.81640625, 0.97265625, 0.9140625, this.field_73735_i);
               GuiHelper.drawAttackInfoMoveset(pixelmon.moveset.get(i), height / 2 + 37, width, height);
            }
         }

         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.replaceattack.effect", new Object[0]), width / 2 - 96, height / 2 + 38, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.description.text", new Object[0]), width / 2 - 20, height / 2 + 22, 16777215);
         GuiHelper.drawPokemonInfoChooseMove(pixelmon, width, height, this.field_73735_i);
      }
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (this.mouseOverBack(width, height, mouseX, mouseY)) {
         this.closeScreen();
      } else {
         PixelmonInGui pixelmon = this.bm.getCurrentPokemon();

         for(int i = 0; i < pixelmon.moveset.size(); ++i) {
            if (this.mouseOverMove(i, width, height, mouseX, mouseY)) {
               this.clickMove(i);
            }
         }

      }
   }

   private boolean mouseOverMove(int i, int width, int height, int mouseX, int mouseY) {
      return mouseX > width / 2 - 30 && mouseX < width / 2 + 120 && mouseY > height / 2 - 94 + 22 * i && mouseY < height / 2 - 94 + 22 * (i + 1);
   }

   private boolean mouseOverBack(int width, int height, int mouseX, int mouseY) {
      return mouseX >= width / 2 + 109 && mouseX <= width / 2 + 121 && mouseY >= height / 2 + 89 && mouseY <= height / 2 + 99;
   }

   protected abstract void clickMove(int var1);

   protected void closeScreen() {
      if (this.bm.battleEnded) {
         this.bm.choosingPokemon = false;
      } else {
         this.bm.mode = BattleMode.MainMenu;
      }

   }
}

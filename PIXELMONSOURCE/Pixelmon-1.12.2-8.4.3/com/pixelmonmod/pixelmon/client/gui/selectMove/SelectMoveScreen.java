package com.pixelmonmod.pixelmon.client.gui.selectMove;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SelectMovePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class SelectMoveScreen extends GuiScreen {
   private GuiScreen parent;
   private Pokemon pokemon;
   private Mode mode;

   public SelectMoveScreen(int mode, int slot) {
      this.field_146297_k = Minecraft.func_71410_x();
      this.parent = null;
      this.pokemon = ClientStorageManager.party.get(slot);
      this.mode = SelectMoveScreen.Mode.values()[mode];
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.pokemon == null) {
         GuiHelper.closeScreen();
      } else {
         this.field_146297_k.field_71446_o.func_110577_a(this.mode == SelectMoveScreen.Mode.REPLACE ? GuiResources.chooseMove : GuiResources.chooseMove2);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 128), (double)(this.field_146295_m / 2 - 102), 256.0, 205.0F, 0.0, 0.0, 1.0, 0.80078125, this.field_73735_i);
         if (this.mouseOverBack(this.field_146294_l, this.field_146295_m, mouseX, mouseY)) {
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 109), (double)(this.field_146295_m / 2 + 89), 12.0, 10.0F, 0.3125, 0.8171206116676331, 0.359375, 0.85546875, this.field_73735_i);
         }

         GuiHelper.drawMoveset(this.pokemon.getMoveset(), this.field_146294_l, this.field_146295_m, this.field_73735_i);

         for(int i = 0; i < this.pokemon.getMoveset().size(); ++i) {
            if (this.mouseOverMove(i, this.field_146294_l, this.field_146295_m, mouseX, mouseY)) {
               this.field_146297_k.field_71446_o.func_110577_a(this.mode == SelectMoveScreen.Mode.REPLACE ? GuiResources.chooseMove : GuiResources.chooseMove2);
               GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 30), (double)(this.field_146295_m / 2 - 94 + 22 * i), 152.0, 24.0F, 0.37890625, 0.81640625, 0.97265625, 0.9140625, this.field_73735_i);
               GuiHelper.drawAttackInfoMoveset(this.pokemon.getMoveset().get(i), this.field_146295_m / 2 + 37, this.field_146294_l, this.field_146295_m);
            }
         }

         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.replaceattack.effect", new Object[0]), this.field_146294_l / 2 - 96, this.field_146295_m / 2 + 38, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.description.text", new Object[0]), this.field_146294_l / 2 - 20, this.field_146295_m / 2 + 22, 16777215);
         GuiHelper.drawPokemonInfoChooseMove(new PixelmonInGui(this.pokemon), this.field_146294_l, this.field_146295_m, this.field_73735_i);
      }
   }

   public void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      if (this.mouseOverBack(this.field_146294_l, this.field_146295_m, mouseX, mouseY)) {
         this.closeScreen();
      } else {
         for(int i = 0; i < this.pokemon.getMoveset().size(); ++i) {
            if (this.mouseOverMove(i, this.field_146294_l, this.field_146295_m, mouseX, mouseY)) {
               this.clickMove(i);
            }
         }

      }
   }

   public boolean func_73868_f() {
      return false;
   }

   private boolean mouseOverMove(int i, int width, int height, int mouseX, int mouseY) {
      return mouseX > width / 2 - 30 && mouseX < width / 2 + 120 && mouseY > height / 2 - 94 + 22 * i && mouseY < height / 2 - 94 + 22 * (i + 1);
   }

   private boolean mouseOverBack(int width, int height, int mouseX, int mouseY) {
      return mouseX >= width / 2 + 109 && mouseX <= width / 2 + 121 && mouseY >= height / 2 + 89 && mouseY <= height / 2 + 99;
   }

   protected void clickMove(int moveIndex) {
      Pixelmon.network.sendToServer(new SelectMovePacket(this.mode.ordinal(), this.pokemon.getUUID(), moveIndex));
      this.closeScreen();
   }

   protected void closeScreen() {
      this.field_146297_k.func_147108_a(this.parent);
   }

   public static enum Mode {
      REPLACE,
      PP_UP,
      CUSTOM;
   }
}

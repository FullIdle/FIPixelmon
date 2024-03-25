package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTextFieldMultipleLine;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;

public class GuiImportExport extends GuiScreen {
   private static final int BUTTON_SAVE = 1;
   private static final int BUTTON_RESET = 2;
   private static final int BUTTON_COPY = 3;
   private static final int BUTTON_PASTE = 4;
   private static final int BUTTON_CANCEL = 5;
   private String titleText;
   private IImportableContainer previousScreen;
   private GuiTextFieldMultipleLine importText;
   private int errorTextTimer;
   private String errorText = "";

   public GuiImportExport(IImportableContainer previousScreen, String titleText) {
      this.previousScreen = previousScreen;
      this.titleText = titleText;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 100, this.field_146295_m / 2 + 90, 40, 20, I18n.func_135052_a("gui.pokemoneditor.save", new Object[0])));
      this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 + 150, this.field_146295_m / 2 - 110, 40, 20, I18n.func_135052_a("gui.pokemoneditor.reset", new Object[0])));
      this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 150, this.field_146295_m / 2 - 80, 40, 20, I18n.func_135052_a("gui.pokemoneditor.copy", new Object[0])));
      this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 150, this.field_146295_m / 2 - 50, 40, 20, I18n.func_135052_a("gui.pokemoneditor.paste", new Object[0])));
      this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 + 150, this.field_146295_m / 2 + 90, 40, 20, I18n.func_135052_a("gui.pokemoneditor.cancel", new Object[0])));
      this.importText = new GuiTextFieldMultipleLine(3, this.field_146294_l / 2 - 80, this.field_146295_m / 2 - 65, 160, 180);
      this.resetText();
   }

   private void resetText() {
      this.importText.func_146180_a(this.previousScreen.getExportText());
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 200), (double)(this.field_146295_m / 2 - 120), 400.0, 240.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      this.field_146297_k.field_71466_p.func_78276_b(this.titleText, this.field_146294_l / 2 - this.field_146297_k.field_71466_p.func_78256_a(this.titleText) / 2, this.field_146295_m / 2 - 90, 0);
      GuiHelper.drawCenteredString(I18n.func_135052_a("gui.pokemoneditor.importexport", new Object[0]), (float)(this.field_146294_l / 2), (float)(this.field_146295_m / 2 - 75), 0);
      this.importText.drawTextBox(mouseX, mouseY);
      if (this.errorTextTimer > 0) {
         --this.errorTextTimer;
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.pokemoneditor.errorimport", new Object[0]), (float)(this.field_146294_l / 2 + 145), (float)(this.field_146295_m / 2 + 65), 16711680);
         GuiHelper.drawCenteredString(this.errorText, (float)(this.field_146294_l / 2 + 145), (float)(this.field_146295_m / 2 + 75), 16711680);
      }

      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   protected void func_73869_a(char key, int keyCode) throws IOException {
      if (keyCode == 1 || keyCode == 28 && !this.importText.func_146206_l()) {
         this.saveAndClose();
      }

      this.importText.func_146201_a(key, keyCode);
   }

   protected void func_73864_a(int x, int y, int z) throws IOException {
      super.func_73864_a(x, y, z);
      this.importText.func_146192_a(x, y, z);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         if (button.field_146127_k == 1) {
            this.saveAndClose();
         } else if (button.field_146127_k == 2) {
            this.resetText();
         } else if (button.field_146127_k == 3) {
            func_146275_d(this.importText.func_146179_b());
         } else if (button.field_146127_k == 4) {
            this.importText.func_146180_a(func_146277_j());
         } else if (button.field_146127_k == 5) {
            this.closeScreen();
         }
      }

   }

   private void saveAndClose() {
      if (this.save()) {
         this.closeScreen();
      } else {
         this.errorTextTimer = 60;
      }

   }

   protected boolean save() {
      this.errorText = this.previousScreen.importText(this.importText.func_146179_b());
      return this.errorText == null;
   }

   private void closeScreen() {
      this.field_146297_k.func_147108_a(this.previousScreen.getScreen());
   }
}

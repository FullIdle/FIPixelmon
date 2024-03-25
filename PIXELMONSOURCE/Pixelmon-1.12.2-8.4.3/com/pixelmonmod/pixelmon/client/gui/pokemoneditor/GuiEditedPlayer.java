package com.pixelmonmod.pixelmon.client.gui.pokemoneditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiChatExtension;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pokemoneditor.RequestCloseEditingPlayer;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;

public class GuiEditedPlayer extends GuiScreen {
   public static UUID editingPlayerUUID;
   public static String editingPlayerName;
   private GuiChatExtension chat = new GuiChatExtension(this, 35);
   private boolean forceClose;
   private static final int BUTTON_CANCEL = 0;

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.chat.drawScreen(mouseX, mouseY, partialTicks);
      super.func_73863_a(mouseX, mouseY, partialTicks);
      GuiHelper.drawCenteredString(I18n.func_74838_a("gui.pokemoneditor.editedplayer").replace("%s", editingPlayerName), (float)(this.field_146294_l / 2), (float)(this.field_146295_m / 2 - 20), 16777215);
   }

   public void func_146282_l() {
      this.chat.handleKeyboardInput();
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.chat.initGui();
      this.field_146292_n.clear();
      this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 25, this.field_146295_m / 2, 50, 20, I18n.func_74838_a("gui.pokemoneditor.cancel")));
   }

   public void func_146281_b() {
      super.func_146281_b();
      this.chat.onGuiClosed();
      if (!this.forceClose) {
         Pixelmon.network.sendToServer(new RequestCloseEditingPlayer(editingPlayerUUID));
         this.forceClose = false;
      }

   }

   public void func_73876_c() {
      super.func_73876_c();
      this.chat.updateScreen(this.field_146295_m);
   }

   protected void func_73869_a(char par1, int par2) {
      this.chat.keyTyped(par1, par2);
   }

   public void func_146274_d() throws IOException {
      try {
         super.func_146274_d();
      } catch (NullPointerException var2) {
         if (PixelmonConfig.printErrors) {
            var2.printStackTrace();
         }

         return;
      }

      this.chat.handleMouseInput();
   }

   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
      this.chat.mouseClicked(par1, par2, par3);
      super.func_73864_a(par1, par2, par3);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146127_k == 0) {
         GuiHelper.closeScreen();
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public void markForceClose() {
      this.forceClose = true;
   }
}

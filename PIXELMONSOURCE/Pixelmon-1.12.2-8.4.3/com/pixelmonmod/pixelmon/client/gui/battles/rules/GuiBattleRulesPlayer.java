package com.pixelmonmod.pixelmon.client.gui.battles.rules;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiAcceptDeny;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiChatExtension;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.AcceptDeclineBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.EnumBattleQueryResponse;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.ProposeBattleRules;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class GuiBattleRulesPlayer extends GuiBattleRulesBase {
   private GuiChatExtension chat;
   private String opponentName = "";
   private int battleQueryID;
   private EnumRulesGuiState state;
   private GuiButton proposeButton;
   private GuiButton acceptButton;
   private GuiButton changeButton;
   private GuiButton declineButton;
   private int flashCounter;
   private static final int FLASH_DURATION = 30;

   public GuiBattleRulesPlayer(int battleQueryID, boolean isProposing) {
      this.battleQueryID = battleQueryID;
      this.state = isProposing ? EnumRulesGuiState.Propose : EnumRulesGuiState.WaitPropose;
      this.chat = new GuiChatExtension(this, 20);
      if (GuiAcceptDeny.opponent != null) {
         EntityPlayer opponentEntity = this.field_146297_k.field_71441_e.func_152378_a(GuiAcceptDeny.opponent.opponentUUID);
         if (opponentEntity != null) {
            this.opponentName = opponentEntity.getDisplayNameString();
         }
      }

      this.yChange = -15;
      this.clauseListHeight = 50;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.chat.initGui();
      int buttonWidth = 60;
      int idCounter = 100;
      this.proposeButton = new GuiButton(idCounter++, this.centerX - buttonWidth / 2, this.centerY + 60, buttonWidth, 20, I18n.func_135052_a("gui.battlerules.propose", new Object[0]));
      this.acceptButton = new GuiButton(idCounter++, this.centerX - buttonWidth, this.centerY + 60, buttonWidth, 20, I18n.func_135052_a("gui.acceptdeny.accept", new Object[0]));
      this.changeButton = new GuiButton(idCounter++, this.centerX, this.centerY + 60, buttonWidth, 20, I18n.func_135052_a("gui.battlerules.change", new Object[0]));
      this.declineButton = new GuiButton(idCounter++, this.centerX + 200 - buttonWidth, this.centerY + 60, buttonWidth, 20, I18n.func_135052_a("gui.acceptdeny.deny", new Object[0]));
      this.field_146292_n.add(this.declineButton);
      this.changeState(this.state);
   }

   public void changeState(EnumRulesGuiState state) {
      this.state = state;
      switch (state) {
         case Propose:
            this.field_146292_n.remove(this.acceptButton);
            this.field_146292_n.remove(this.changeButton);
            this.enableButton(this.proposeButton);
            this.editingEnabled = true;
            break;
         case WaitPropose:
         case WaitChange:
         case WaitAccept:
            this.field_146292_n.remove(this.proposeButton);
            this.field_146292_n.remove(this.acceptButton);
            this.field_146292_n.remove(this.changeButton);
            this.editingEnabled = false;
            break;
         case Accept:
            this.field_146292_n.remove(this.proposeButton);
            this.enableButton(this.acceptButton);
            this.enableButton(this.changeButton);
            this.editingEnabled = false;
      }

   }

   private void enableButton(GuiButton button) {
      if (!this.field_146292_n.contains(button)) {
         this.field_146292_n.add(button);
      }

   }

   public void func_73876_c() {
      super.func_73876_c();
      this.chat.updateScreen(this.field_146295_m);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.chat.drawScreen(mouseX, mouseY, partialTicks);
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
      if (this.state != EnumRulesGuiState.Propose) {
         this.dimScreen();
         String langKey = "";
         int textColor = 0;
         switch (this.state) {
            case WaitPropose:
               langKey = "gui.battlerules.waitpropose";
               break;
            case WaitChange:
               langKey = "gui.battlerules.waitchange";
               break;
            case WaitAccept:
               langKey = "gui.battlerules.waitaccept";
         }

         if ((double)(this.flashCounter++) >= 45.0) {
            this.flashCounter = 0;
         }

         boolean isWaiting = this.state.isWaiting();
         if (!isWaiting) {
            this.highlightButtons(80, 35);
         }

         if (this.flashCounter < 30 || !isWaiting) {
            GuiHelper.drawCenteredString(I18n.func_135052_a(langKey, new Object[]{this.opponentName}), (float)this.centerX, (float)(this.rectBottom - 30), textColor);
         }
      }

   }

   protected int getBackgroundHeight() {
      return 200;
   }

   public void func_146270_b(int tint) {
   }

   public void func_146282_l() throws IOException {
      super.func_146282_l();
      this.chat.handleKeyboardInput();
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      this.chat.handleMouseInput();
   }

   protected void mouseClickedUnderMenus(int x, int y, int mouseButton) throws IOException {
      super.mouseClickedUnderMenus(x, y, mouseButton);
      this.chat.mouseClicked(x, y, mouseButton);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         if (button == this.proposeButton) {
            this.registerRules();
            Pixelmon.network.sendToServer(new ProposeBattleRules(this.battleQueryID, this.rules));
         } else if (button == this.acceptButton) {
            Pixelmon.network.sendToServer(new AcceptDeclineBattle(this.battleQueryID, EnumBattleQueryResponse.Accept));
         } else if (button == this.changeButton) {
            Pixelmon.network.sendToServer(new AcceptDeclineBattle(this.battleQueryID, EnumBattleQueryResponse.Change));
         } else if (button == this.declineButton) {
            Pixelmon.network.sendToServer(new AcceptDeclineBattle(this.battleQueryID, EnumBattleQueryResponse.Decline));
            GuiHelper.closeScreen();
         }

      }
   }

   public void func_146281_b() {
      super.func_146281_b();
      this.chat.onGuiClosed();
   }

   public boolean func_73868_f() {
      return false;
   }
}

package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.rules.GuiBattleRulesBase;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.NPCServerPacket;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import java.io.IOException;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;

public class GuiBattleRulesNPCEditor extends GuiBattleRulesBase {
   private static final int BUTTON_ID_OK = 100;

   public GuiBattleRulesNPCEditor() {
      Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, GuiTrainerEditor.currentTrainerID, NPCTrainer.class);
      if (!entityNPCOptional.isPresent()) {
         GuiHelper.closeScreen();
      } else {
         this.rules = ((NPCTrainer)entityNPCOptional.get()).battleRules;
      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.add(new GuiButton(100, this.centerX + 155, this.centerY + 90, 30, 20, I18n.func_74838_a("gui.guiItemDrops.ok")));
   }

   protected void func_73869_a(char key, int keyCode) {
      super.func_73869_a(key, keyCode);
      if (keyCode == 1 || keyCode == 28) {
         this.closeScreen();
      }

   }

   protected void drawBackgroundUnderMenus(float partialTicks, int mouseX, int mouseY) {
      super.drawBackgroundUnderMenus(partialTicks, mouseX, mouseY);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146127_k == 100) {
         this.closeScreen();
      }

   }

   private void closeScreen() {
      this.registerRules();
      Pixelmon.network.sendToServer(new NPCServerPacket(GuiTrainerEditor.currentTrainerID, this.rules));
      this.field_146297_k.func_147108_a(new GuiTrainerEditor(GuiTrainerEditor.currentTrainerID));
   }
}

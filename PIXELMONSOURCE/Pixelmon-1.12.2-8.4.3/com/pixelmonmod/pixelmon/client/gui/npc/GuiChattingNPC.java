package com.pixelmonmod.pixelmon.client.gui.npc;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.translation.I18n;

public class GuiChattingNPC extends GuiScreen {
   public static ArrayList chatPages;
   public static String name;
   int currentPage = 0;
   int currentTrainerID;

   public GuiChattingNPC(int trainerId) {
      Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, trainerId, NPCChatting.class);
      if (!entityNPCOptional.isPresent()) {
         Minecraft.func_71410_x().field_71439_g.func_71053_j();
      } else {
         entityNPCOptional.get();
         this.currentTrainerID = trainerId;
      }
   }

   public void func_73863_a(int i, int j, float f) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      ArrayList chatText = new ArrayList();
      if (this.currentPage < chatPages.size()) {
         chatText.add(I18n.func_74838_a((String)chatPages.get(this.currentPage)));
      }

      GuiHelper.drawDialogueBox(this, (String)name, (List)chatText, this.field_73735_i);
      GlStateManager.func_179084_k();
   }

   protected void func_73869_a(char key, int par2) throws IOException {
      super.func_73869_a(key, par2);
      if (par2 == 28) {
         this.continueDialogue();
      }

   }

   protected void func_73864_a(int x, int y, int z) throws IOException {
      super.func_73864_a(x, y, z);
      this.continueDialogue();
   }

   private void continueDialogue() {
      ++this.currentPage;
      if (this.currentPage > chatPages.size() - 1) {
         Minecraft.func_71410_x().field_71439_g.func_71053_j();
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
   }
}

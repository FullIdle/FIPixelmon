package com.pixelmonmod.pixelmon.client.gui.custom.dialogue;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiRoundButton;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueChoiceMade;
import com.pixelmonmod.pixelmon.comm.packetHandlers.dialogue.DialogueClosure;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiDialogue extends GuiScreen {
   private static volatile List dialogues;
   private static volatile UUID quest = null;
   private List choiceButtons = new ArrayList();
   private Dialogue currentDialogue;
   private boolean pause = false;
   protected int guiLeft;
   protected int guiTop;

   public GuiDialogue() {
      this.currentDialogue = (Dialogue)dialogues.get(0);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.guiLeft = (this.field_146294_l - 176) / 2;
      this.guiTop = (this.field_146295_m - 166) / 2;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      if (this.currentDialogue != null) {
         if (this.choiceButtons.isEmpty() && !this.currentDialogue.choices.isEmpty()) {
            this.loadButtons();
         }

         GuiHelper.drawDialogueBox(this, (String)this.currentDialogue.name, (List)Lists.newArrayList(new String[]{this.currentDialogue.getText()}), 0.0F);

         for(int i = 0; i < this.choiceButtons.size(); ++i) {
            ((GuiRoundButton)this.choiceButtons.get(i)).drawButton(this.getButtonX(i), this.getButtonY(i), mouseX, mouseY, 0.0F);
         }

         super.func_73863_a(mouseX, mouseY, partialTicks);
         GlStateManager.func_179084_k();
      }
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      if (!this.pause) {
         if (this.currentDialogue.choices.isEmpty()) {
            removeImmediateDialogue();
            this.next();
            if (this.currentDialogue != null) {
               this.loadButtons();
            }

         } else {
            for(int i = 0; i < this.choiceButtons.size(); ++i) {
               if (((GuiRoundButton)this.choiceButtons.get(i)).isMouseOver(this.getButtonX(i), this.getButtonY(i), mouseX, mouseY)) {
                  this.pause = true;
                  Pixelmon.network.sendToServer(new DialogueChoiceMade((Choice)this.currentDialogue.choices.get(i)));
                  break;
               }
            }

         }
      }
   }

   public boolean func_73868_f() {
      return false;
   }

   public void close() {
      Minecraft.func_71410_x().field_71439_g.func_71053_j();
   }

   public void next() {
      this.pause = false;
      if (dialogues.isEmpty()) {
         this.close();
      } else {
         this.currentDialogue = (Dialogue)dialogues.get(0);
         this.choiceButtons.clear();
      }

   }

   public static void removeImmediateDialogue() {
      if (!dialogues.isEmpty()) {
         dialogues.remove(0);
      }

   }

   public static void addDialogues(List dialogues) {
      GuiDialogue.dialogues.addAll(dialogues);
   }

   public static void insertDialogues(List dialogues) {
      dialogues.addAll(GuiDialogue.dialogues);
      GuiDialogue.dialogues = dialogues;
   }

   public static void setDialogues(List dialogues) {
      setDialogues(dialogues, (UUID)null);
   }

   public static void setDialogues(List dialogues, UUID quest) {
      GuiDialogue.dialogues = dialogues;
      GuiDialogue.quest = quest;
   }

   public static boolean isQuest() {
      boolean isQuest = quest != null;
      quest = null;
      return isQuest;
   }

   private void loadButtons() {
      this.choiceButtons.clear();

      for(int i = 0; i < this.currentDialogue.choices.size(); ++i) {
         this.choiceButtons.add(new GuiRoundButton(-30 + (230 - (this.getLargestWidth() + 20)) / 2, 65, ((Choice)this.currentDialogue.choices.get(i)).text, this.getLargestWidth() + 20, 20));
      }

   }

   private int getLargestWidth() {
      int max = 0;

      for(int i = 0; i < this.currentDialogue.choices.size(); ++i) {
         if (this.field_146297_k.field_71466_p.func_78256_a(((Choice)this.currentDialogue.choices.get(i)).text) > max) {
            max = this.field_146297_k.field_71466_p.func_78256_a(((Choice)this.currentDialogue.choices.get(i)).text);
         }
      }

      return max;
   }

   private int getButtonX(int buttonNum) {
      return this.guiLeft;
   }

   private int getButtonY(int buttonNum) {
      return this.guiTop + 23 * buttonNum;
   }

   public void func_146281_b() {
      Pixelmon.network.sendToServer(new DialogueClosure());
   }
}

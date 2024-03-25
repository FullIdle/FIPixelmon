package com.pixelmonmod.pixelmon.quests.client.editor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiListQuestEditor;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiSimpleButton;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiTextField;
import com.pixelmonmod.pixelmon.quests.comm.editor.ReloadQuestData;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

public class GuiQuestEditor extends GuiScreen {
   private List buttons = new ArrayList();
   private GuiListQuestEditor questList;
   private GuiTextField newQuest;
   private boolean canMakeQuest = false;
   private String madeQuest = "";
   private Quest selected = null;
   int prevWidth = -1;
   int prevHeight = -1;

   public GuiQuestEditor() {
   }

   public GuiQuestEditor(Quest selected) {
      this.selected = selected;
   }

   public void addButtons() {
      this.buttons.clear();
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int buttonS = true;
      this.buttons.add(new GuiSimpleButton(0, x + 4, y + 6, 12, GuiResources.refresh, 2, 2, -5635841));
   }

   public void func_73866_w_() {
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int gap = true;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int newQuestY = y + dispHeight - 35 + 7;
      this.questList = new GuiListQuestEditor(this.field_146297_k.field_71466_p, x + 7, y + 23, 104, 9);
      this.questList.setFocused(true);
      this.newQuest = new GuiTextField(this.field_146297_k.field_71466_p, x + 1, newQuestY + 4, 95, 20, -6908266, -7895161);
      this.newQuest.setTextColor(14889216);
      this.refreshQuests();
      if (this.selected != null) {
         boolean found = false;
         Iterator var10 = QuestEditorState.get().getAllQuests().iterator();

         while(var10.hasNext()) {
            Quest quest = (Quest)var10.next();
            if (quest.getPrintableName().equalsIgnoreCase(this.selected.getPrintableName())) {
               this.selected = quest;
               found = true;
               break;
            }
         }

         if (found) {
            int lineindex = this.questList.lines.indexOf(this.selected);
            this.questList.selectQuest(lineindex);
            if (lineindex >= 0 && lineindex < this.questList.lines.size()) {
               QuestEditorState.get().selectQuest((Quest)this.questList.lines.get(lineindex));
            } else {
               QuestEditorState.get().selectQuest((Quest)null);
            }
         }

         this.selected = null;
      }

      this.addButtons();
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179141_d();
      GlStateManager.func_179112_b(770, 771);
      super.func_73863_a(mouseX, mouseY, f);
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.backgroundTexture);
      int gap = 3;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int div = 2 * gap;
      int border = 25;
      int lowerBorder = dispHeight - border - div;
      int qlh = 132;
      this.questList.height = qlh;
      this.questList.displayLines = (int)(9.0F * ((float)qlh / (float)(lowerBorder - border - 12)));
      int newQuestGap = 35;
      Gui.func_146110_a(x + dispWidth / 2 + gap + 1, y, 0.0F, 0.0F, dispWidth / 2, dispHeight, (float)dispWidth / 2.0F, (float)dispHeight);
      Gui.func_146110_a(x - gap, y, 0.0F, 0.0F, dispWidth / 2, dispHeight - newQuestGap, (float)dispWidth / 2.0F, (float)(dispHeight - newQuestGap));
      int newQuestY = y + dispHeight - newQuestGap + 7;
      Gui.func_146110_a(x - gap, newQuestY, 0.0F, 0.0F, dispWidth / 2, dispHeight - (dispHeight - newQuestGap) - 7, (float)dispWidth / 2.0F, (float)(dispHeight - 30));
      String textA = TextFormatting.BOLD + I18n.func_135052_a("gui.questeditor.title", new Object[0]);
      this.field_146289_q.func_175065_a(textA, (float)(x - gap) + (float)dispWidth / 4.0F - (float)this.field_146289_q.func_78256_a(textA) / 2.0F, (float)y + 8.5F, 16777215, true);
      this.questList.drawBackground();
      this.questList.drawText();
      this.newQuest.drawTextBox();
      float titleBorder = 8.5F;
      int addQuestY;
      if (QuestEditorState.get().hasQuestSelected()) {
         Quest selectedQuest = QuestEditorState.get().getSelectedQuest();
         String title = selectedQuest.getPrintableName();
         int width = (int)((float)dispWidth / 2.0F - 9.0F);
         int strWidth = this.field_146289_q.func_78256_a(title);
         double wf = 95.0;
         double finalWidth = Math.min((double)strWidth, wf - 6.0);
         GuiHelper.drawSquashedString(this.field_146289_q, title, false, wf, (float)((int)((double)((float)(x + gap) + (float)dispWidth / 4.0F * 3.0F) - finalWidth / 2.0 + 6.0)), (float)((int)((float)y + titleBorder)), 16777215, true);
         this.field_146297_k.field_71446_o.func_110577_a(((Stage)selectedQuest.getStages().get(selectedQuest.getStages().size() - 1)).getStage() == selectedQuest.getActiveStage() ? GuiResources.question_mark : GuiResources.exclamation_mark);
         QuestColor color = selectedQuest.getColor();
         GlStateManager.func_179131_c(color.floatR(), color.floatG(), color.floatB(), 1.0F);
         Gui.func_146110_a((int)((double)((float)(x + gap) + (float)dispWidth / 4.0F * 3.0F) - finalWidth / 2.0) - 11, (int)((float)y + titleBorder) - 4, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);

         for(int i = 0; i < 4; ++i) {
            int buttonW = 50;
            int buttonH = 30;
            int buttonX = x + (int)((float)dispWidth / 4.0F * 3.0F) + gap + 1 - buttonW / 2;
            int buttonY = y + 30 + i * 42;
            String text = "";
            switch (i) {
               case 0:
                  func_73734_a(buttonX, buttonY, buttonX + buttonW, buttonY + buttonH, -16727066);
                  text = I18n.func_135052_a("gui.questeditor.edit", new Object[0]);
                  break;
               case 1:
                  func_73734_a(buttonX, buttonY, buttonX + buttonW, buttonY + buttonH, -10263709);
                  text = I18n.func_135052_a("gui.questeditor.test", new Object[0]);
                  break;
               case 2:
                  func_73734_a(buttonX, buttonY, buttonX + buttonW, buttonY + buttonH, -6029082);
                  text = I18n.func_135052_a("gui.questeditor.clone", new Object[0]);
                  break;
               case 3:
                  func_73734_a(buttonX, buttonY, buttonX + buttonW, buttonY + buttonH, -2424803);
                  text = I18n.func_135052_a("gui.questeditor.delete", new Object[0]);
            }

            int textWidth = this.field_146289_q.func_78256_a(text);
            GuiHelper.drawSquashedString(this.field_146289_q, text, false, wf, (float)((int)((float)buttonX - (float)textWidth / 2.0F + (float)buttonW / 2.0F)), (float)((int)((float)(buttonY - 4) + (float)buttonH / 2.0F)), 16777215, true);
         }
      } else {
         String title = I18n.func_135052_a("gui.questeditor.noselection", new Object[0]);
         addQuestY = this.field_146289_q.func_78256_a(title);
         GuiHelper.drawSquashedString(this.field_146289_q, title, false, (double)addQuestY, (float)((int)((float)(x + gap) + (float)dispWidth / 4.0F * 3.0F - (float)addQuestY / 2.0F)), (float)((int)((float)y + titleBorder)), 16777215, true);
      }

      int addQuestX = x + 104;
      addQuestY = y + dispHeight - 19;
      int size = 10;
      func_73734_a(addQuestX, addQuestY, addQuestX + size, addQuestY + size, this.canMakeQuest ? -16724221 : -10066330);
      this.field_146289_q.func_175065_a("+", (float)addQuestX + (float)size / 2.0F - 2.5F, (float)addQuestY + (float)size / 2.0F - 3.5F, 16777215, false);
      Iterator var39 = this.buttons.iterator();

      while(var39.hasNext()) {
         GuiSimpleButton button = (GuiSimpleButton)var39.next();
         button.draw();
      }

      this.checkMouseWheel();
      GlStateManager.func_179084_k();
      GlStateManager.func_179118_c();
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int gap = 3;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int div = 2 * gap;
      int border = 25;
      int lowerBorder = dispHeight - border - div;
      float v = (float)(x + gap * 3) + 3.0F * (float)dispWidth / 4.0F;
      int sliderSpeed = true;
      int id;
      byte buttonW;
      if (this.canMakeQuest) {
         int addQuestX = x + 104;
         id = y + dispHeight - 19;
         buttonW = 10;
         if (mouseX >= addQuestX && mouseX <= addQuestX + buttonW && mouseY >= id && mouseY <= id + buttonW) {
            String questName = this.newQuest.getText() + ".json";
            this.madeQuest = questName;
            QuestEditorState.get().push(questName, new Quest(questName), false);
            this.canMakeQuest = false;
         }
      }

      Quest selectedQuest;
      if (QuestEditorState.get().hasQuestSelected()) {
         selectedQuest = QuestEditorState.get().getSelectedQuest();

         for(id = 0; id < 4; ++id) {
            buttonW = 50;
            int buttonH = 30;
            int buttonX = x + (int)((float)dispWidth / 4.0F * 3.0F) + gap + 1 - buttonW / 2;
            int buttonY = y + 30 + id * 42;
            if (mouseX >= buttonX && mouseX <= buttonX + buttonW && mouseY >= buttonY && mouseY <= buttonY + buttonH) {
               switch (id) {
                  case 0:
                     this.field_146297_k.func_147108_a(new GuiQuestOverview());
                  case 1:
                  default:
                     break;
                  case 2:
                     StringBuilder copy = new StringBuilder("Copy");

                     while(QuestEditorState.get().hasQuest(selectedQuest.getPrintableName() + " " + copy.toString())) {
                        copy.append(" Copy");
                     }

                     Quest newQuest = new Quest(selectedQuest, selectedQuest.getPrintableName() + " " + copy.toString() + ".json");
                     this.madeQuest = newQuest.getFilename();
                     QuestEditorState.get().push(newQuest.getFilename(), newQuest, false);
                     break;
                  case 3:
                     QuestEditorState.get().delete(selectedQuest);
               }
            }
         }
      }

      selectedQuest = this.questList.mouseClicked(mouseX, mouseY, 0, this.questList.yPos + 8);
      if (selectedQuest != null) {
         id = this.questList.lines.indexOf(selectedQuest);
         this.questList.selectQuest(id);
         if (id >= 0 && id < this.questList.lines.size()) {
            QuestEditorState.get().selectQuest((Quest)this.questList.lines.get(id));
         } else {
            QuestEditorState.get().selectQuest((Quest)null);
         }
      }

      id = -1;
      Iterator var25 = this.buttons.iterator();

      while(var25.hasNext()) {
         GuiSimpleButton button = (GuiSimpleButton)var25.next();
         if (button.isEnabled() && button.isWithin(mouseX, mouseY)) {
            id = button.id;
            break;
         }
      }

      if (id == 0) {
         Pixelmon.network.sendToServer(new ReloadQuestData());
      }

      this.newQuest.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void func_73869_a(char c, int i) {
      try {
         super.func_73869_a(c, i);
      } catch (IOException var4) {
         if (Pixelmon.devEnvironment) {
            Pixelmon.LOGGER.error(var4.getLocalizedMessage());
         }
      }

      if (this.newQuest.isFocused() && this.newQuest.textboxKeyTyped(c, i)) {
         this.updateNewQuestBox();
      }

   }

   public void updateNewQuestBox() {
      this.refreshQuests();
      if (this.newQuest.getText().isEmpty()) {
         this.newQuest.setTextColor(14889216);
         this.canMakeQuest = false;
      } else {
         String filename = this.newQuest.getText().replace(" ", "_");
         Iterator var2 = QuestEditorState.get().getAllQuests().iterator();

         Quest quest;
         do {
            if (!var2.hasNext()) {
               this.newQuest.setTextColor(Color.WHITE.getRGB());
               this.canMakeQuest = true;
               return;
            }

            quest = (Quest)var2.next();
         } while(!quest.getIdentityName().equalsIgnoreCase(filename));

         this.newQuest.setTextColor(14889216);
         this.canMakeQuest = false;
      }
   }

   public void refreshQuests() {
      int size = this.questList.lines.size();
      this.questList.lines.clear();
      Iterator var2 = QuestEditorState.get().getAllQuests().iterator();

      while(var2.hasNext()) {
         Quest quest = (Quest)var2.next();
         this.questList.addLine(quest);
      }

      this.questList.sort(this.newQuest.getText());
      if (size > this.questList.lines.size()) {
         this.questList.selectQuest(-1);
         QuestEditorState.get().selectQuest((Quest)null);
      }

      if (!this.madeQuest.isEmpty()) {
         int i = 0;

         for(Iterator var6 = this.questList.lines.iterator(); var6.hasNext(); ++i) {
            Quest quest = (Quest)var6.next();
            if (quest.getFilename().equalsIgnoreCase(this.madeQuest)) {
               QuestEditorState.get().selectQuest(quest);
               this.questList.selectQuest(i);
               break;
            }
         }

         this.madeQuest = "";
      }

      if (Math.abs(size - this.questList.lines.size()) > 1) {
         this.questList.startLine = 0;
      }

   }

   private void checkMouseWheel() {
      int mousewheelDirection = Mouse.getDWheel();
      if (mousewheelDirection == 120) {
         if (this.questList.isFocused) {
            this.questList.scrollDown();
         }
      } else if (mousewheelDirection == -120 && this.questList.isFocused) {
         this.questList.scrollUp();
      }

   }

   public void func_73876_c() {
      if (QuestEditorState.get().isDirty()) {
         this.refreshQuests();
         this.updateNewQuestBox();
      }

      if (this.prevWidth != -1 && this.prevHeight != -1 && (this.field_146294_l != this.prevWidth || this.field_146295_m != this.prevHeight)) {
         this.addButtons();
      }

      this.prevWidth = this.field_146294_l;
      this.prevHeight = this.field_146295_m;
   }
}

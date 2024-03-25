package com.pixelmonmod.pixelmon.quests.client.editor;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiListQuestLang;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiSimpleButton;
import com.pixelmonmod.pixelmon.quests.client.editor.ui.GuiTextField;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class GuiQuestStrings extends GuiScreen {
   private List buttons = new ArrayList();
   private GuiListQuestLang list;
   private GuiTextField field;
   private String selection = null;
   private boolean reload = false;
   int prevWidth = -1;
   int prevHeight = -1;

   public void func_73866_w_() {
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int gap = true;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      float titleBorder = 5.5F;
      int listW = 150;
      this.list = new GuiListQuestLang(this.field_146297_k.field_71466_p, x + dispWidth / 2 - listW / 2 - 4, y + 15, listW, 9);
      this.list.setFocused(true);
      ArrayList keys = new ArrayList(QuestEditorState.get().getSelectedQuest().getUnlocalizedStringMap().keySet());
      Collections.sort(keys);
      Iterator var11 = keys.iterator();

      while(var11.hasNext()) {
         String key = (String)var11.next();
         this.list.addLine(key);
      }

      this.field_146297_k.field_71466_p.func_78264_a(true);
      this.field = new GuiTextField(this.field_146297_k.field_71466_p, x, y + dispHeight - 25, dispWidth - 6, 9, -6908266, -7895161);
      this.field.setMaxStringLength(Integer.MAX_VALUE);
      this.field.setTextColor(-1);
      if (this.selection != null) {
         this.field.setText((String)QuestEditorState.get().getSelectedQuest().getUnlocalizedStringMap().get(this.selection));
      }

      this.field_146297_k.field_71466_p.func_78264_a(false);
      this.addButtons();
   }

   public void addButtons() {
      this.buttons.clear();
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      this.buttons.add(new GuiSimpleButton(0, x + 4, y + 6, 12, GuiResources.back, 2, 2, -12407307));
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      if (QuestEditorState.get().hasQuestSelected()) {
         Quest quest = QuestEditorState.get().getSelectedQuest();
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
         this.list.height = qlh;
         this.list.displayLines = (int)(9.0F * ((float)qlh / (float)(lowerBorder - border - 12)));
         Gui.func_146110_a(x - gap, y, 0.0F, 0.0F, dispWidth, dispHeight, (float)dispWidth, (float)dispHeight);
         GlStateManager.func_179147_l();
         GlStateManager.func_179141_d();
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179084_k();
         GlStateManager.func_179118_c();
         Iterator var16 = this.buttons.iterator();

         while(var16.hasNext()) {
            GuiSimpleButton button = (GuiSimpleButton)var16.next();
            button.draw();
         }

         this.list.drawBackground();
         this.list.drawText();
         this.field_146297_k.field_71466_p.func_78264_a(true);
         this.field.drawTextBox();
         this.field_146297_k.field_71466_p.func_78264_a(false);
         GuiHelper.drawCenteredString(this.selection == null ? "None Selected" : prettifyLangKey(this.selection), (float)x + (float)dispWidth / 2.0F, (float)(y + dispHeight) - 37.5F, 16777215);
         this.checkMouseWheel(mouseX, mouseY);
      }
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      int xOffset = 20;
      int yOffset = 20;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 180;
      int x = this.field_146294_l / 2 - 125;
      int y = this.field_146295_m / 2 - 100;
      int id = -1;
      Iterator var11 = this.buttons.iterator();

      while(var11.hasNext()) {
         GuiSimpleButton button = (GuiSimpleButton)var11.next();
         if (button.isEnabled() && button.isWithin(mouseX, mouseY)) {
            id = button.id;
            break;
         }
      }

      if (id != -1) {
         this.field_146297_k.func_147108_a(new GuiQuestOverview());
      }

      String clickedOn = this.list.mouseClicked(mouseX, mouseY, 0, this.list.yPos + 8);
      if (clickedOn != null) {
         int lineindex = this.list.lines.indexOf(clickedOn);
         this.list.select(lineindex);
         if (lineindex >= 0 && lineindex < this.list.lines.size()) {
            this.selection = (String)this.list.lines.get(lineindex);
            this.field.setText((String)QuestEditorState.get().getSelectedQuest().getUnlocalizedStringMap().get(this.selection));
         } else {
            this.selection = null;
         }
      }

      this.field_146297_k.field_71466_p.func_78264_a(true);
      this.field.mouseClicked(mouseX, mouseY, mouseButton);
      this.field_146297_k.field_71466_p.func_78264_a(false);
   }

   public void func_73869_a(char c, int i) {
      try {
         super.func_73869_a(c, i);
      } catch (IOException var4) {
         if (Pixelmon.devEnvironment) {
            Pixelmon.LOGGER.error(var4.getLocalizedMessage());
         }
      }

      this.field_146297_k.field_71466_p.func_78264_a(true);
      if (this.field.isFocused() && this.field.textboxKeyTyped(c, i) && this.selection != null) {
         QuestEditorState.get().getSelectedQuest().putUnlocalizedString(this.selection, this.field.getText());
      }

      this.field_146297_k.field_71466_p.func_78264_a(false);
   }

   private void checkMouseWheel(int mouseX, int mouseY) {
      int mousewheelDirection = Mouse.getDWheel();
      if (mousewheelDirection == 120) {
         if (this.list.isFocused) {
            this.list.scrollDown();
         }
      } else if (mousewheelDirection == -120 && this.list.isFocused) {
         this.list.scrollUp();
      }

   }

   public void func_73876_c() {
      if (!QuestEditorState.get().hasQuestSelected()) {
         this.field_146297_k.func_147108_a(new GuiQuestEditor());
      }

      if (this.prevWidth != -1 && this.prevHeight != -1 && (this.field_146294_l != this.prevWidth || this.field_146295_m != this.prevHeight)) {
         this.func_73866_w_();
      }

      if (QuestEditorState.get().isDirty() && this.reload) {
         this.reload = false;
      }

      this.prevWidth = this.field_146294_l;
      this.prevHeight = this.field_146295_m;
   }

   public static String prettifyLangKey(String line) {
      boolean changed = false;

      try {
         if (line.startsWith("desc-")) {
            String stage = line.replace("desc-", "");
            if (stage.equalsIgnoreCase("X")) {
               line = "Quest Complete Description";
            } else {
               line = "Stage " + stage + " Description";
            }

            changed = true;
         } else if (line.equalsIgnoreCase("name")) {
            line = "Quest Name";
            changed = true;
         } else {
            String[] split;
            if (line.startsWith("stage-")) {
               split = line.split("-");
               line = "Stage " + split[1] + " Objective " + (Integer.parseInt(split[2]) + 1);
               changed = true;
            } else if (line.matches("[A-z]+[0-9]+-[A-z]+[0-9]+-[A-z]+[0-9]+")) {
               split = line.split("-");
               line = "Stage " + split[0].substring(1) + " Objective " + (Integer.parseInt(split[1].substring(1)) + 1) + " Element " + (Integer.parseInt(split[2].substring(1)) + 1);
               changed = true;
            }
         }
      } catch (Exception var3) {
      }

      if (!changed) {
         line = "String Key " + line;
      }

      return line;
   }
}

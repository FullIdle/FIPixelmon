package com.pixelmonmod.pixelmon.quests.client.editor.ui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.quests.quest.Quest;
import com.pixelmonmod.pixelmon.quests.quest.QuestColor;
import com.pixelmonmod.pixelmon.quests.quest.Stage;
import java.awt.Color;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiListQuestEditor extends Gui {
   private static final ResourceLocation circle = new ResourceLocation("pixelmon", "textures/gui/quests/circle.png");
   private static final ResourceLocation exclamation_mark = new ResourceLocation("pixelmon", "textures/gui/quests/exclamation_mark.png");
   private static final ResourceLocation question_mark = new ResourceLocation("pixelmon", "textures/gui/quests/question_mark.png");
   public static final int X_OFFSET = 4;
   public static final int Y_OFFSET = 2;
   private final FontRenderer fRend;
   public int xPos = 0;
   public int yPos = 0;
   public int width = 12;
   public int height = 12;
   public boolean drawBackground = true;
   public boolean drawBorder = true;
   public boolean isEnabled = true;
   public boolean isFocused = false;
   public int borderColor = -6250336;
   public int backgroundColor = 15921906;
   public int selectedLineColor = 5066061;
   public int textColor;
   public int selectedTextColor;
   public int displayLines;
   public int lineHeight;
   public List lines;
   public int startLine;
   public Integer selectedQuest;
   public boolean highlightSelectedLine;

   public GuiListQuestEditor(FontRenderer fontRenderer, int x, int y, int w, int lines) {
      this.textColor = Color.BLACK.getRGB();
      this.selectedTextColor = Color.WHITE.getRGB();
      this.displayLines = 0;
      this.lineHeight = 10;
      this.lines = new LinkedList();
      this.startLine = 0;
      this.selectedQuest = null;
      this.highlightSelectedLine = true;
      this.fRend = fontRenderer;
      this.xPos = x;
      this.yPos = y;
      this.displayLines = lines;
      this.lineHeight = fontRenderer.field_78288_b + 6;
      this.width = w;
      this.height = this.displayLines * this.lineHeight + 2;
   }

   public boolean sort(String search) {
      boolean changedSize = false;
      if (!search.isEmpty()) {
         changedSize = this.lines.removeIf((quest) -> {
            return !quest.getPrintableName().toLowerCase().startsWith(search.toLowerCase());
         });
      }

      this.lines.sort(Comparator.comparing(Quest::getIdentityName));
      return changedSize;
   }

   public void drawBackground() {
      if (this.lines.size() > 9 && this.displayLines == 9) {
         int bottomMarker = this.lineHeight * 9 - 1;
         float barMult = 9.0F / ((float)this.lines.size() - 8.0F);
         float barHeight = (float)this.lineHeight * barMult;
         float barPos = barHeight * (float)this.startLine;
         func_73734_a(this.xPos + this.width + 2, this.yPos + 3 + (int)barPos, this.xPos + this.width + 6, this.yPos + 3 + (int)barPos + (int)barHeight, -6710887);
         func_73734_a(this.xPos + this.width + 2, this.yPos + 1, this.xPos + this.width + 6, this.yPos + 3, -7829368);
         func_73734_a(this.xPos + this.width + 2, this.yPos + 1 + bottomMarker, this.xPos + this.width + 6, this.yPos + 3 + bottomMarker, -7829368);
      }

   }

   public void drawText() {
      Minecraft mc = Minecraft.func_71410_x();

      for(int i = this.startLine; i < this.startLine + this.displayLines; ++i) {
         if (i < this.lines.size() && i >= 0) {
            Quest quest = (Quest)this.lines.get(i);
            if (quest != null) {
               double wf = 85.0;
               if (this.selectedQuest != null && this.selectedQuest == i && this.highlightSelectedLine) {
                  func_73734_a(this.xPos, this.yPos + 1 + this.lineHeight * (i - this.startLine), this.xPos + this.width, this.yPos + this.lineHeight * (1 + i - this.startLine) + this.lineHeight / 4 - 1, -7829368);
                  GuiHelper.drawSquashedString(this.fRend, quest.getPrintableName(), false, wf, (float)(this.xPos + 4 + 12), (float)(this.yPos + 2 + this.lineHeight * (i - this.startLine) + this.lineHeight / 4), this.selectedTextColor, true);
               } else {
                  GuiHelper.drawSquashedString(this.fRend, quest.getPrintableName(), false, wf, (float)(this.xPos + 4 + 12), (float)(this.yPos + 2 + this.lineHeight * (i - this.startLine) + this.lineHeight / 4), this.selectedTextColor, false);
               }

               GlStateManager.func_179147_l();
               GlStateManager.func_179141_d();
               GlStateManager.func_179112_b(770, 771);
               mc.field_71446_o.func_110577_a(((Stage)quest.getStages().get(quest.getStages().size() - 1)).getStage() == quest.getActiveStage() ? question_mark : exclamation_mark);
               if (quest.getColor() == null) {
                  GlStateManager.func_179131_c(0.8509804F, 0.8509804F, 0.8509804F, 1.0F);
               } else {
                  QuestColor color = quest.getColor();
                  GlStateManager.func_179131_c(color.floatR(), color.floatG(), color.floatB(), 1.0F);
               }

               Gui.func_146110_a(this.xPos + 1, this.yPos + 1 + this.lineHeight * (i - this.startLine) + 1, 0.0F, 0.0F, 14, 14, 14.0F, 14.0F);
            }
         }
      }

   }

   public Quest mouseClicked(int mouseX, int mouseY, int mouseButton, int offsetY) {
      if (mouseX > this.xPos && mouseX < this.xPos + this.width && mouseY > this.yPos && mouseY < this.yPos + this.height) {
         mouseY += 8;
         int theLine = (mouseY - offsetY) / this.lineHeight;
         int index = theLine + this.startLine;
         if (index < this.lines.size() && index >= 0) {
            return (Quest)this.lines.get(index);
         }
      }

      return null;
   }

   public void setEnabled(boolean enable) {
      this.isEnabled = enable;
   }

   public void setFocused(boolean focus) {
      this.isFocused = focus;
   }

   public void addLine(Quest quest) {
      this.lines.add(quest);
   }

   public void selectQuest(int i) {
      this.selectedQuest = i;
   }

   public void scrollUp() {
      ++this.startLine;
      if (this.startLine > this.lines.size() - this.displayLines) {
         this.startLine = this.lines.size() - this.displayLines;
      }

      if (this.startLine < 0) {
         this.startLine = 0;
      }

   }

   public void scrollDown() {
      --this.startLine;
      if (this.startLine < 0) {
         this.startLine = 0;
      }

   }
}

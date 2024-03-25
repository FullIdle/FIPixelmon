package com.pixelmonmod.pixelmon.quests.client.editor.ui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.quests.client.editor.GuiQuestStrings;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class GuiListQuestLang extends Gui {
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
   public Integer selected;
   public boolean highlightSelectedLine;

   public GuiListQuestLang(FontRenderer fontRenderer, int x, int y, int w, int lines) {
      this.textColor = Color.BLACK.getRGB();
      this.selectedTextColor = Color.WHITE.getRGB();
      this.displayLines = 0;
      this.lineHeight = 10;
      this.lines = new LinkedList();
      this.startLine = 0;
      this.selected = null;
      this.highlightSelectedLine = true;
      this.fRend = fontRenderer;
      this.xPos = x;
      this.yPos = y;
      this.displayLines = lines;
      this.lineHeight = fontRenderer.field_78288_b + 6;
      this.width = w;
      this.height = this.displayLines * this.lineHeight + 2;
   }

   public void drawBackground() {
      int bottomMarker = this.lineHeight * 9 - 1;
      func_73734_a(this.xPos, this.yPos + 1, this.xPos + this.width + 6, this.yPos + 3 + bottomMarker, -10066330);
      if (this.lines.size() > 9 && this.displayLines == 9) {
         float barMult = 9.0F / ((float)this.lines.size() - 8.0F);
         float barHeight = (float)this.lineHeight * barMult;
         float barPos = barHeight * (float)this.startLine;
         func_73734_a(this.xPos + this.width, this.yPos + 3 + (int)barPos, this.xPos + this.width + 6, this.yPos + 3 + (int)barPos + (int)barHeight, -6710887);
         func_73734_a(this.xPos + this.width, this.yPos + 1, this.xPos + this.width + 6, this.yPos + 3, -7829368);
         func_73734_a(this.xPos + this.width, this.yPos + 1 + bottomMarker, this.xPos + this.width + 6, this.yPos + 3 + bottomMarker, -7829368);
      }

   }

   public void drawText() {
      Minecraft mc = Minecraft.func_71410_x();

      for(int i = this.startLine; i < this.startLine + this.displayLines; ++i) {
         if (i < this.lines.size() && i >= 0) {
            String line = (String)this.lines.get(i);
            if (line != null) {
               double wf = 125.0;
               line = GuiQuestStrings.prettifyLangKey(line);
               if (this.selected != null && this.selected == i && this.highlightSelectedLine) {
                  func_73734_a(this.xPos, this.yPos + 1 + this.lineHeight * (i - this.startLine), this.xPos + this.width, this.yPos + this.lineHeight * (1 + i - this.startLine) + this.lineHeight / 4 - 1, -7829368);
                  GuiHelper.drawSquashedString(this.fRend, line, false, wf, (float)(this.xPos + 4 + 12), (float)(this.yPos + 2 + this.lineHeight * (i - this.startLine) + this.lineHeight / 4), this.selectedTextColor, true);
               } else {
                  GuiHelper.drawSquashedString(this.fRend, line, false, wf, (float)(this.xPos + 4 + 12), (float)(this.yPos + 2 + this.lineHeight * (i - this.startLine) + this.lineHeight / 4), this.selectedTextColor, false);
               }
            }
         }
      }

   }

   public String mouseClicked(int mouseX, int mouseY, int mouseButton, int offsetY) {
      if (mouseX > this.xPos && mouseX < this.xPos + this.width && mouseY > this.yPos && mouseY < this.yPos + this.height) {
         mouseY += 8;
         int theLine = (mouseY - offsetY) / this.lineHeight;
         int index = theLine + this.startLine;
         if (index < this.lines.size() && index >= 0) {
            return (String)this.lines.get(index);
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

   public void addLine(String line) {
      this.lines.add(line);
   }

   public void select(int i) {
      this.selected = i;
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

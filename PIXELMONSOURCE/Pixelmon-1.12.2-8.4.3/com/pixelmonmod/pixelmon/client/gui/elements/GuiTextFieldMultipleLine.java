package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiTextFieldMultipleLine extends GuiTextField {
   final FontRenderer fontRendererInstance;
   private int enabledColor = 14737632;
   private int cursorCounter;
   List splitString = new ArrayList();
   private GuiTextScrollBar scrollBar;
   private int mouseX;
   private int mouseY;
   private boolean scrollToCursor;

   public GuiTextFieldMultipleLine(int componentID, int x, int y, int width, int height) {
      super(componentID, Minecraft.func_71410_x().field_71466_p, x, y, width, height);
      Minecraft mc = Minecraft.func_71410_x();
      this.fontRendererInstance = mc.field_71466_p;
      this.func_146203_f(Integer.MAX_VALUE);
      this.scrollBar = new GuiTextScrollBar(this);
   }

   public void func_146178_a() {
      super.func_146178_a();
      ++this.cursorCounter;
   }

   public void func_146191_b(String addText) {
      addText.replace("\r\n", "\n").replace("\r", "\n");
      if (addText.contains("\n")) {
         String newText = "";
         int cursorPosition = this.func_146198_h();
         int selectionEnd = this.func_146186_n();
         String text = this.func_146179_b();
         int start = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
         int end = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
         int length = false;
         if (!text.isEmpty()) {
            newText = newText + text.substring(0, start);
         }

         newText = newText + addText;
         int length = addText.length();
         if (text.length() > 0 && end < text.length()) {
            newText = newText + text.substring(end);
         }

         this.func_146180_a(newText);
         this.func_146190_e(cursorPosition);
         this.func_146182_d(start - selectionEnd + length);
      } else {
         super.func_146191_b(addText);
      }

   }

   public boolean func_146192_a(int x, int y, int buttonClicked) {
      boolean retur = super.func_146192_a(x, y, buttonClicked);
      boolean mouseInTextField = x >= this.field_146209_f && x < this.field_146209_f + this.field_146218_h && y >= this.field_146210_g && y < this.field_146210_g + this.field_146219_i;
      if (this.func_146206_l() && mouseInTextField && buttonClicked == 0) {
         int lineOffset = this.fontRendererInstance.field_78288_b + 2;
         int cursorLine = (y - this.field_146210_g - 4) / lineOffset + this.scrollBar.getTopIndex();
         this.moveCursor(cursorLine, x);
      }

      return retur;
   }

   private void moveCursor(int line, int x) {
      if (line < 0) {
         this.func_146196_d();
      } else if (line >= this.splitString.size()) {
         this.func_146202_e();
      } else {
         int newPosition = 0;

         for(int i = 0; i < line; ++i) {
            newPosition += ((String)this.splitString.get(i)).length();
         }

         String currentLine = (String)this.splitString.get(line);
         int currentWidth = 0;
         int mouseXOffset = x - this.field_146209_f - 4;
         if (mouseXOffset > 0) {
            int linePosition = -1;
            int currentLineLength = currentLine.length();

            for(int i = 0; i < currentLineLength; ++i) {
               if (currentWidth > mouseXOffset) {
                  linePosition = i - 1;
                  break;
               }

               currentWidth += this.fontRendererInstance.func_78263_a(currentLine.charAt(i));
            }

            if (linePosition == -1) {
               if (!currentLine.isEmpty() && currentLine.charAt(currentLineLength - 1) == '\n') {
                  linePosition = currentLineLength - 1;
               } else {
                  linePosition = currentLineLength;
               }
            }

            newPosition += linePosition;
         }

         this.func_146190_e(newPosition);
      }

      this.scrollToCursor = true;
   }

   public boolean func_146201_a(char key, int keyCode) {
      this.scrollToCursor = true;
      if (key != '\r' && key != '\n' && keyCode != 156) {
         if (keyCode != 200 && keyCode != 208) {
            return super.func_146201_a(key, keyCode);
         } else {
            int[] cursorStringData = this.getSplitStringPosition(this.func_146198_h());
            int cursorLine = cursorStringData[0];
            int cursorX = this.getInitialXPosition() + cursorStringData[1];
            if (keyCode == 200) {
               this.moveCursor(cursorLine - 1, cursorX);
            } else {
               this.moveCursor(cursorLine + 1, cursorX);
            }

            return true;
         }
      } else {
         this.func_146191_b("\n");
         return true;
      }
   }

   public void drawTextBox(int mouseX, int mouseY) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      this.func_146194_f();
   }

   private int getInitialXPosition() {
      return this.func_146181_i() ? this.field_146209_f + 4 : this.field_146209_f;
   }

   int getLineHeight() {
      return this.fontRendererInstance.field_78288_b + 2;
   }

   public void func_146194_f() {
      if (this.func_146176_q()) {
         boolean enableBackgroundDrawing = this.func_146181_i();
         int cursorPosition = this.func_146198_h();
         int selectionEnd = this.func_146186_n();
         String text = this.func_146179_b();
         boolean isFocused = this.func_146206_l();
         if (enableBackgroundDrawing) {
            func_73734_a(this.field_146209_f - 1, this.field_146210_g - 1, this.field_146209_f + this.field_146218_h + 1, this.field_146210_g + this.field_146219_i + 1, -6250336);
            func_73734_a(this.field_146209_f, this.field_146210_g, this.field_146209_f + this.field_146218_h, this.field_146210_g + this.field_146219_i, -16777216);
         }

         int textColor = this.enabledColor;
         boolean cursorInBox = cursorPosition >= 0 && cursorPosition <= text.length();
         boolean cursorVisible = isFocused && this.cursorCounter / 6 % 2 == 0 && cursorInBox;
         int currentXPosition = this.getInitialXPosition();
         int currentYPosition = this.field_146210_g + this.fontRendererInstance.field_78288_b;
         if (enableBackgroundDrawing) {
            currentYPosition -= 6;
         }

         int initialYPosition = currentYPosition;
         selectionEnd = Math.min(selectionEnd, text.length());
         int maxWidth = enableBackgroundDrawing ? this.field_146218_h - 8 : this.field_146218_h;
         int lineOffset = this.getLineHeight();
         this.splitString = GuiHelper.splitString(text, maxWidth);
         int[] cursorStringData = this.getSplitStringPosition(cursorPosition);
         int cursorLine = cursorStringData[0];
         int endSelectionLine = -1;
         int endSelectionX = 0;
         if (selectionEnd != cursorPosition) {
            int[] endSelectionData = this.getSplitStringPosition(selectionEnd);
            endSelectionLine = endSelectionData[0];
            endSelectionX = endSelectionData[1];
         }

         int cursorX = currentXPosition + cursorStringData[1];
         if (this.scrollToCursor) {
            if (endSelectionLine > -1 && endSelectionLine != cursorLine) {
               this.scrollBar.scrollTo(endSelectionLine);
            } else {
               this.scrollBar.scrollTo(cursorLine);
            }

            this.scrollToCursor = false;
         }

         int topIndex = this.scrollBar.getTopIndex();
         int bottomIndex = this.scrollBar.getBottomIndex();

         int cursorYPosition;
         for(cursorYPosition = topIndex; cursorYPosition <= bottomIndex; ++cursorYPosition) {
            this.fontRendererInstance.func_175063_a(GuiHelper.removeNewLine((String)this.splitString.get(cursorYPosition)), (float)currentXPosition, (float)currentYPosition, textColor);
            currentYPosition += lineOffset;
         }

         currentYPosition -= lineOffset;
         cursorYPosition = initialYPosition + (cursorLine - topIndex) * lineOffset;
         boolean drawCursorBar = cursorPosition < text.length() || text.length() >= this.func_146208_g();
         int selectionStartX = cursorX;
         if (!cursorInBox) {
            selectionStartX = cursorPosition > 0 ? currentXPosition + this.field_146218_h : currentXPosition;
         }

         if (cursorVisible && cursorLine >= topIndex && cursorLine <= bottomIndex) {
            if (drawCursorBar) {
               Gui.func_73734_a(selectionStartX - 1, cursorYPosition - 1, selectionStartX, cursorYPosition + 1 + this.fontRendererInstance.field_78288_b, -3092272);
            } else {
               this.fontRendererInstance.func_175063_a("_", (float)selectionStartX, (float)currentYPosition, textColor);
            }
         }

         if (selectionEnd != cursorPosition) {
            int i;
            int left;
            int right;
            if (endSelectionLine > cursorLine) {
               cursorLine = Math.max(cursorLine, topIndex);
               endSelectionLine = Math.min(endSelectionLine, bottomIndex + 1);

               for(i = cursorLine; i <= endSelectionLine; ++i) {
                  left = i == cursorLine ? selectionStartX : currentXPosition;
                  right = currentXPosition + (i == endSelectionLine ? endSelectionX : this.fontRendererInstance.func_78256_a(GuiHelper.removeNewLine((String)this.splitString.get(i))));
                  currentYPosition = initialYPosition + (i - topIndex) * lineOffset;
                  this.drawCursorVertical(right, currentYPosition - 1, left, currentYPosition + 1 + this.fontRendererInstance.field_78288_b);
               }
            } else {
               endSelectionLine = Math.max(endSelectionLine, topIndex);
               cursorLine = Math.min(cursorLine, bottomIndex + 1);

               for(i = endSelectionLine; i <= cursorLine; ++i) {
                  left = currentXPosition;
                  if (i == endSelectionLine) {
                     left = currentXPosition + endSelectionX;
                  }

                  right = i == cursorLine ? selectionStartX : currentXPosition + this.fontRendererInstance.func_78256_a(GuiHelper.removeNewLine((String)this.splitString.get(i)));
                  currentYPosition = initialYPosition + (i - topIndex) * lineOffset;
                  this.drawCursorVertical(right, currentYPosition - 1, left, currentYPosition + 1 + this.fontRendererInstance.field_78288_b);
               }
            }
         }

         this.scrollBar.drawScreen(this.mouseX, this.mouseY, 0.0F);
      }

   }

   private int[] getSplitStringPosition(int position) {
      int currentPosition = 0;
      int numLines = this.splitString.size();
      int positionLine = numLines - 1;
      int positionOffset = -1;

      int lineWidth;
      for(lineWidth = 0; lineWidth < numLines; ++lineWidth) {
         int newPosition = currentPosition + ((String)this.splitString.get(lineWidth)).length();
         if (newPosition > position) {
            positionLine = lineWidth;
            positionOffset = position - currentPosition;
            break;
         }

         currentPosition = newPosition;
      }

      if (positionOffset == -1) {
         positionOffset = ((String)this.splitString.get(positionLine)).length();
      }

      lineWidth = this.fontRendererInstance.func_78256_a(GuiHelper.removeNewLine(((String)this.splitString.get(positionLine)).substring(0, positionOffset)));
      return new int[]{positionLine, lineWidth};
   }

   private void drawCursorVertical(int right, int top, int left, int bottom) {
      int j;
      if (right < left) {
         j = right;
         right = left;
         left = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      if (left != right) {
         --left;
      }

      if (left > this.field_146209_f + this.field_146218_h) {
         left = this.field_146209_f + this.field_146218_h;
      }

      if (right > this.field_146209_f + this.field_146218_h) {
         right = this.field_146209_f + this.field_146218_h;
      }

      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder vertexBuffer = tessellator.func_178180_c();
      GlStateManager.func_179131_c(0.0F, 0.0F, 255.0F, 255.0F);
      GlStateManager.func_179090_x();
      GlStateManager.func_179115_u();
      GlStateManager.func_179116_f(5387);
      vertexBuffer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
      vertexBuffer.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
      vertexBuffer.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
      vertexBuffer.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
      vertexBuffer.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179134_v();
      GlStateManager.func_179098_w();
   }

   public void func_146193_g(int newColor) {
      super.func_146193_g(newColor);
      this.enabledColor = newColor;
   }
}

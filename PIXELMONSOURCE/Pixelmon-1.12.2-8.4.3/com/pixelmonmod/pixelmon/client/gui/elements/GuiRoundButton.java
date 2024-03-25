package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import net.minecraft.client.Minecraft;

public class GuiRoundButton {
   private int leftOffset;
   private int topOffset;
   private String text;
   private int id;
   private int width;
   private int height;

   public GuiRoundButton(int leftOffset, int topOffset, String text) {
      this.width = 100;
      this.height = 20;
      this.leftOffset = leftOffset;
      this.topOffset = topOffset;
      this.setText(text);
   }

   public GuiRoundButton(int leftOffset, int topOffset, String text, int width, int height) {
      this(leftOffset, topOffset, text);
      this.width = width;
      this.height = height;
   }

   public void setText(String text) {
      this.text = text;
   }

   public int getId() {
      return this.id;
   }

   public GuiRoundButton setId(int id) {
      this.id = id;
      return this;
   }

   public void drawButton(int leftX, int topY, int mouseX, int mouseY, float zLevel) {
      this.drawButton(leftX, topY, mouseX, mouseY, zLevel, false);
   }

   public void drawButton(int leftX, int topY, int mouseX, int mouseY, float zLevel, boolean forceOutline) {
      Minecraft mc = Minecraft.func_71410_x();
      mc.field_71446_o.func_110577_a(GuiResources.roundedButton);
      int left = leftX + this.leftOffset;
      int top = topY + this.topOffset;
      GuiHelper.drawImageQuad((double)left, (double)top, (double)this.width, (float)this.height, 0.0, 0.0, 1.0, 1.0, zLevel);
      mc.field_71446_o.func_110577_a(GuiResources.roundedButtonOver);
      if (forceOutline) {
         int outside = 5;
         GuiHelper.drawImageQuad((double)(left - outside), (double)(top - outside), (double)(this.width + outside * 2), (float)(this.height + outside * 2), 0.0, 0.0, 1.0, 1.0, zLevel);
      } else if (this.isMouseOver(leftX, topY, mouseX, mouseY)) {
         GuiHelper.drawImageQuad((double)left, (double)top, (double)this.width, (float)this.height, 0.0, 0.0, 1.0, 1.0, zLevel);
      }

      GuiHelper.drawCenteredString(this.text, (float)(left + this.width / 2), (float)(top + 6), 16777215);
   }

   public boolean isMouseOver(int leftX, int topY, int mouseX, int mouseY) {
      int left = leftX + this.leftOffset;
      int top = topY + this.topOffset;
      return mouseX > left && mouseX < left + this.width && mouseY > top && mouseY < top + this.height;
   }
}

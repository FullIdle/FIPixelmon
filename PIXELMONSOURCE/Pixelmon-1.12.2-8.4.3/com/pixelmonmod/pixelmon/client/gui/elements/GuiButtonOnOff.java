package com.pixelmonmod.pixelmon.client.gui.elements;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;

public class GuiButtonOnOff extends GuiButton {
   private boolean on;

   public GuiButtonOnOff(int buttonID, int x, int y, int width, int height, boolean on) {
      super(buttonID, x, y, width, height, "");
      this.on = on;
      this.updateText();
   }

   public boolean toggle() {
      this.setOn(!this.on);
      return this.on;
   }

   private void updateText() {
      this.field_146126_j = I18n.func_74838_a("gui.battlerules." + (this.on ? "on" : "off"));
   }

   public boolean isOn() {
      return this.on;
   }

   public void setOn(boolean on) {
      this.on = on;
      this.updateText();
   }
}

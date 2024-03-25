package com.pixelmonmod.pixelmon.client.gui.elements;

import net.minecraft.client.renderer.Tessellator;

class GuiTextScrollBar extends GuiSlotBase {
   private GuiTextFieldMultipleLine textField;

   GuiTextScrollBar(GuiTextFieldMultipleLine textField) {
      super(textField.field_146210_g - 2, textField.field_146209_f, textField.field_146218_h, textField.field_146219_i, true);
      this.textField = textField;
      this.slotHeight = textField.getLineHeight();
   }

   protected int getSize() {
      return this.textField.splitString.size();
   }

   protected void elementClicked(int index, boolean doubleClicked) {
   }

   protected boolean isSelected(int element) {
      return false;
   }

   protected void drawSlot(int index, int x, int yTop, int yMiddle, Tessellator tessellator) {
   }

   protected float[] get1Color() {
      return new float[]{0.0F, 0.0F, 0.0F};
   }

   protected int[] getSelectionColor() {
      return null;
   }

   protected int[] get255Color() {
      return null;
   }
}

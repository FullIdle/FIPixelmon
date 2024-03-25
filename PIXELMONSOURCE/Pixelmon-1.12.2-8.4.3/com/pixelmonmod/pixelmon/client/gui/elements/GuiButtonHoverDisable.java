package com.pixelmonmod.pixelmon.client.gui.elements;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonHoverDisable extends GuiButton {
   private boolean hoverDisabled;

   public GuiButtonHoverDisable(int buttonId, int x, int y, String buttonText) {
      super(buttonId, x, y, buttonText);
   }

   public GuiButtonHoverDisable(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      super(buttonId, x, y, widthIn, heightIn, buttonText);
   }

   public void setHoverDisabled(boolean hoverDisabled) {
      this.hoverDisabled = hoverDisabled;
   }

   protected int func_146114_a(boolean mouseOver) {
      int hoverState = super.func_146114_a(mouseOver);
      if (hoverState == 2 && this.hoverDisabled) {
         hoverState = 1;
      }

      return hoverState;
   }

   public void func_73732_a(FontRenderer fontRendererIn, String text, int x, int y, int color) {
      if (this.hoverDisabled) {
         color = 14737632;
         if (this.packedFGColour != 0) {
            color = this.packedFGColour;
         } else if (!this.field_146124_l) {
            color = 10526880;
         }
      }

      super.func_73732_a(fontRendererIn, text, x, y, color);
   }

   public static void setHoverDisabledScreen(List buttonList, boolean hoverDisabled) {
      Iterator var2 = buttonList.iterator();

      while(var2.hasNext()) {
         GuiButton button = (GuiButton)var2.next();
         if (button instanceof GuiButtonHoverDisable) {
            ((GuiButtonHoverDisable)button).setHoverDisabled(hoverDisabled);
         }
      }

   }
}

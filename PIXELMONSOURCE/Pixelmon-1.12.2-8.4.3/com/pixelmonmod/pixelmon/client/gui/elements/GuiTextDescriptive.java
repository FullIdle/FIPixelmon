package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;

public class GuiTextDescriptive {
   private String text;
   private String description;
   private int x;
   private int y;
   private EnumTextAlign align;
   private Rectangle boundingBox;

   public GuiTextDescriptive(String text, String description, int x, int y, EnumTextAlign align) {
      this.text = text;
      this.description = description;
      this.x = x;
      this.y = y;
      this.align = align;
      int width = Minecraft.func_71410_x().field_71466_p.func_78256_a(text);
      int height = 10;
      int boxX = x;
      switch (align) {
         case Left:
            boxX = x;
            break;
         case Center:
            boxX = x - width / 2;
            break;
         case Right:
            boxX = x - width;
      }

      this.boundingBox = new Rectangle(boxX, y, width, height);
   }

   public void draw() {
      switch (this.align) {
         case Left:
            Minecraft.func_71410_x().field_71466_p.func_78276_b(this.text, this.x, this.y, 0);
            break;
         case Center:
            GuiHelper.drawCenteredString(this.text, (float)this.x, (float)this.y, 0);
            break;
         case Right:
            GuiHelper.drawStringRightAligned(this.text, (float)this.x, (float)this.y, 0, false);
      }

   }

   public boolean isHovering(int cursorX, int cursorY) {
      return this.boundingBox.contains(cursorX, cursorY);
   }

   public String getDescription() {
      return this.description;
   }
}

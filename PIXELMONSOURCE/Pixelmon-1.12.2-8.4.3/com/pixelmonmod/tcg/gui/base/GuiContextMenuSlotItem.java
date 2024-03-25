package com.pixelmonmod.tcg.gui.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import org.lwjgl.input.Mouse;

public abstract class GuiContextMenuSlotItem {
   public int x;
   public int y;
   protected final Minecraft mc;
   protected final GuiScreen guiScreen;
   protected final GuiSlot guiSlot;
   protected final int width;
   protected final int height;
   private boolean isVisible = false;
   public final List buttonList = new ArrayList();
   private boolean isHandlingMousePressed = false;

   public GuiContextMenuSlotItem(Minecraft mc, GuiScreen guiScreen, GuiSlot guiSlot, int width, int height) {
      this.mc = mc;
      this.guiScreen = guiScreen;
      this.guiSlot = guiSlot;
      this.width = width;
      this.height = height;
   }

   public void updateScreen() {
      if (this.isVisible && Mouse.isCreated()) {
         if (Mouse.getEventButtonState() && Mouse.isButtonDown(0)) {
            if (!this.isHandlingMousePressed) {
               this.isHandlingMousePressed = true;
               int mouseX = Mouse.getEventX() * this.guiScreen.field_146294_l / this.mc.field_71443_c;
               int mouseY = this.guiScreen.field_146295_m - Mouse.getEventY() * this.guiScreen.field_146295_m / this.mc.field_71440_d - 1;
               if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height) {
                  Iterator var3 = this.buttonList.iterator();

                  while(var3.hasNext()) {
                     GuiButton button = (GuiButton)var3.next();
                     if (button.func_146116_c(this.mc, mouseX, mouseY)) {
                        this.actionPerformed(button);
                     }
                  }
               } else {
                  this.isVisible = false;
                  this.guiSlot.func_148143_b(true);
               }
            }
         } else {
            this.isHandlingMousePressed = false;
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY) {
      if (this.isVisible) {
         Iterator var3 = this.buttonList.iterator();

         while(var3.hasNext()) {
            GuiButton button = (GuiButton)var3.next();
            button.func_191745_a(this.mc, mouseX, mouseY, this.guiScreen.field_146297_k.func_184121_ak());
         }
      }

   }

   protected void actionPerformed(GuiButton button) {
   }

   public boolean isVisible() {
      return this.isVisible;
   }

   public void setVisible(boolean visible) {
      this.isVisible = visible;
   }
}

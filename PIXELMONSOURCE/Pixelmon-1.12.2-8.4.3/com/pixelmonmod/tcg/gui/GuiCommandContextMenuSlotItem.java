package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.tcg.gui.base.GuiContextMenuSlotItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;

public class GuiCommandContextMenuSlotItem extends GuiContextMenuSlotItem {
   private static final int SIZE_X = 60;
   private static final int SIZE_Y = 30;
   protected GuiButton buttonDelete = new GuiButton(1001, 0, 0, 50, 20, "Delete");

   public GuiCommandContextMenuSlotItem(Minecraft mc, GuiScreen guiScreen, GuiSlot guiSlot) {
      super(mc, guiScreen, guiSlot, 60, 30);
      this.buttonList.add(this.buttonDelete);
   }

   public void setVisible(boolean visible) {
      if (visible) {
         this.buttonDelete.field_146128_h = this.x + 5;
         this.buttonDelete.field_146129_i = this.y + 5;
      }

      super.setVisible(visible);
   }

   protected void actionPerformed(GuiButton button) {
      switch (button.field_146127_k) {
         case 1001:
            if (this.guiSlot instanceof GuiCommandSlot) {
               GuiCommandSlot friendList = (GuiCommandSlot)this.guiSlot;
               friendList.deleteClicked();
            }
         default:
            super.actionPerformed(button);
      }
   }
}

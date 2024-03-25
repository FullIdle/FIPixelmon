package com.pixelmonmod.pixelmon.client.gui.elements;

import java.util.Iterator;
import java.util.List;
import net.minecraft.util.NonNullList;
import org.lwjgl.input.Mouse;

public class GuiDropDownManager {
   private final List dropDowns = NonNullList.func_191196_a();
   private boolean lastMouse;

   public void drawDropDowns(float partialTicks, int mouseX, int mouseY) {
      GuiDropDown active = null;
      Iterator var7 = this.dropDowns.iterator();

      GuiDropDown dropDown;
      while(var7.hasNext()) {
         dropDown = (GuiDropDown)var7.next();
         if (dropDown != null && dropDown.active) {
            active = dropDown;
            if (dropDown.isMouseOver(mouseX, mouseY)) {
               mouseY = -1;
               mouseX = -1;
            }
            break;
         }
      }

      for(int i = 0; i < this.dropDowns.size(); ++i) {
         dropDown = (GuiDropDown)this.dropDowns.get(i);
         if (dropDown != null && !dropDown.active && dropDown.getVisible()) {
            dropDown.drawScreen(mouseX, mouseY, partialTicks);
         }
      }

      if (active != null) {
         active.drawScreen(mouseX, mouseY, partialTicks);
      }

      this.lastMouse = Mouse.isButtonDown(0);
   }

   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
      boolean selected = false;

      for(int i = 0; i < this.dropDowns.size(); ++i) {
         GuiDropDown dropDown = (GuiDropDown)this.dropDowns.get(i);
         if (dropDown != null) {
            if (dropDown.isMouseOver(mouseX, mouseY)) {
               selected = true;
            } else {
               dropDown.active = false;
            }

            selected = selected || dropDown.getLastSelected();
         }
      }

      return selected;
   }

   public void addDropDown(GuiDropDown dropDown) {
      dropDown.setManager(this);
      this.dropDowns.add(dropDown);
   }

   public void removeDropDown(GuiDropDown dropDown) {
      this.dropDowns.remove(dropDown);
   }

   public void clearDropDowns() {
      this.dropDowns.clear();
   }

   public boolean isMouseOver(int mouseX, int mouseY) {
      return this.dropDowns != null && !this.dropDowns.isEmpty() ? this.dropDowns.stream().anyMatch((dropDown) -> {
         return dropDown != null && dropDown.isMouseOver(mouseX, mouseY);
      }) : false;
   }

   boolean getLastMouse() {
      return this.lastMouse;
   }
}

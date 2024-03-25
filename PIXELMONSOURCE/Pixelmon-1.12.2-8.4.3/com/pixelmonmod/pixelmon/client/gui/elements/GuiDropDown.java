package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import java.awt.Rectangle;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class GuiDropDown {
   List options;
   int selectedIndex;
   Object selectedOption;
   Consumer onSelected;
   Consumer onSelectedIndex;
   Function getOptionString;
   private GuiDropDownSlot slot;
   boolean active;
   private boolean lastSelected;
   private int top;
   private Rectangle rect;
   private Rectangle iconRect;
   EnumTextAlign align;
   private GuiDropDownManager manager;
   private static final int ICON_LENGTH = 11;
   public static final int OPTION_HEIGHT = 10;
   private boolean visible;

   public GuiDropDown(List options, int selected, int left, int top, int width, int height) {
      this(options, left, top, width, height, (double)width);
      this.setOptions(options, selected);
   }

   public GuiDropDown(List options, int selected, int left, int top, int width, int height, int dropDownWidth) {
      this(options, left, top, width, height, (double)dropDownWidth);
      this.setOptions(options, selected);
   }

   public GuiDropDown(List options, Object selected, int left, int top, int width, int height) {
      this(options, left, top, width, height, (double)width);
      this.setOptions(options, selected);
   }

   public GuiDropDown(List options, Object selected, int left, int top, int width, int height, int dropDownWidth) {
      this(options, left, top, width, height, (double)dropDownWidth);
      this.setOptions(options, selected);
   }

   private GuiDropDown(List options, int left, int top, int width, int height, double dropDownWidth) {
      this.selectedIndex = -1;
      this.align = EnumTextAlign.Left;
      this.visible = true;
      height = Math.min(height, 10 * options.size() + 4);
      this.top = top;
      this.slot = new GuiDropDownSlot(this, top - 3, left, width, height, (int)dropDownWidth);
      int rectTop = top + 1;
      this.rect = new Rectangle(left, rectTop, width, 10);
      this.iconRect = new Rectangle(left + width, rectTop, 11, 11);
   }

   void setManager(GuiDropDownManager manager) {
      this.manager = manager;
   }

   private void setOptions(List options, Object selected) {
      this.options = options;
      this.setSelected(selected);
   }

   private void setOptions(List options, int selected) {
      this.options = options;
      this.setSelected(selected);
   }

   public void setSelected(Object selected) {
      this.selectedOption = selected;

      for(int i = 0; i < this.options.size(); ++i) {
         if (this.options.get(i).equals(selected)) {
            this.selectedIndex = i;
            this.slot.scrollTo(i);
            break;
         }
      }

   }

   public void setSelected(int selected) {
      this.selectedIndex = selected;
      this.slot.scrollTo(selected);
      if (this.selectedIndex >= 0 && this.selectedIndex < this.options.size()) {
         this.selectedOption = this.options.get(selected);
      } else {
         this.selectedOption = null;
      }

   }

   public GuiDropDown setInactiveTop(int top) {
      this.top = top;
      int rectTop = top + 1;
      this.rect.y = rectTop;
      this.iconRect.y = rectTop;
      return this;
   }

   public GuiDropDown setTextAlign(EnumTextAlign align) {
      this.align = align;
      return this;
   }

   public GuiDropDown setGetOptionString(Function getOptionString) {
      this.getOptionString = getOptionString;
      return this;
   }

   public GuiDropDown setOnSelected(Consumer onSelected) {
      this.onSelected = onSelected;
      return this;
   }

   public GuiDropDown setOnSelectedIndex(Consumer onSelectedIndex) {
      this.onSelectedIndex = onSelectedIndex;
      return this;
   }

   public GuiDropDown setOrdered() {
      this.options.sort((o1, o2) -> {
         return this.toOptionString(o1).compareTo(this.toOptionString(o2));
      });
      if (this.selectedIndex >= 0) {
         for(int i = 0; i < this.options.size(); ++i) {
            if (this.options.get(i) == this.selectedOption) {
               this.setSelected(i);
               break;
            }
         }
      }

      return this;
   }

   public Object getSelected() {
      return this.selectedOption;
   }

   void mouseClicked(int mouseX, int mouseY) {
   }

   void elementClicked(int index) {
      if (!this.manager.getLastMouse()) {
         if (this.active) {
            this.lastSelected = true;
            this.selectedIndex = index;
            this.selectedOption = this.options.get(index);
            if (this.onSelected != null) {
               this.onSelected.accept(this.selectedOption);
            }

            if (this.onSelectedIndex != null) {
               this.onSelectedIndex.accept(index);
            }

            this.active = false;
         } else {
            this.active = true;
         }

      }
   }

   String toOptionString(Object option) {
      return this.getOptionString == null ? option.toString() : (String)this.getOptionString.apply(option);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179140_f();
      float gray = this.active ? 0.6F : 1.0F;
      GlStateManager.func_179131_c(gray, gray, gray, 1.0F);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(GuiResources.dropDownIcon);
      GuiHelper.drawImageQuad((double)(this.slot.left + this.slot.width), (double)this.top, 11.0, 11.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
      GlStateManager.func_179121_F();
      boolean mouseDown = Mouse.isButtonDown(0);
      boolean lastMouse = this.manager.getLastMouse();
      boolean mouseFirstPressed = mouseDown && !lastMouse;
      this.lastSelected = this.lastSelected && lastMouse;
      if (this.active) {
         this.slot.drawScreen(mouseX, mouseY, partialTicks);
         if (mouseFirstPressed && this.iconRect.contains(mouseX, mouseY) && (!this.slot.hasScrollBar() || !this.slot.inBoundsScroll(mouseX, mouseY))) {
            this.active = false;
         }
      } else {
         int slotTop = this.top + 1;
         this.slot.drawBackgroundRect(this.top, slotTop + 10, -1644826);
         if (this.isMouseOver(mouseX, mouseY)) {
            this.slot.drawSelection(6, this.top + 1);
            if (mouseFirstPressed) {
               this.active = true;
            }
         }

         this.slot.drawOptionString(this.selectedOption, slotTop);
      }

   }

   public boolean isMouseOver(int mouseX, int mouseY) {
      if (this.iconRect.contains(mouseX, mouseY)) {
         return true;
      } else if (this.active) {
         return this.slot.hasScrollBar() ? this.slot.inBoundsScroll(mouseX, mouseY) : this.slot.inBounds(mouseX, mouseY);
      } else {
         return this.rect.contains(mouseX, mouseY);
      }
   }

   public boolean getLastSelected() {
      return this.lastSelected;
   }

   public int getTop() {
      return this.top;
   }

   public int getSelectedIndex() {
      return this.selectedIndex;
   }

   public boolean getVisible() {
      return this.visible;
   }

   public void setVisible(boolean isVisible) {
      this.visible = isVisible;
   }
}

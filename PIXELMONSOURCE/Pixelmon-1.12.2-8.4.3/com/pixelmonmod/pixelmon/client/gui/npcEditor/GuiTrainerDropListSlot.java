package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.pixelmonmod.pixelmon.client.gui.elements.GuiSlotBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;

public class GuiTrainerDropListSlot extends GuiSlotBase {
   GuiTrainerEditorMore gui;

   public GuiTrainerDropListSlot(GuiTrainerEditorMore gui) {
      super(gui.listTop, gui.listLeft, 120, gui.listHeight, false);
      this.gui = gui;
   }

   protected int getSize() {
      return this.gui.getDropListCount();
   }

   protected void elementClicked(int var1, boolean var2) {
      this.gui.removeFromList(var1);
   }

   protected boolean isSelected(int var1) {
      return false;
   }

   protected float[] get1Color() {
      return new float[]{0.7F, 0.7F, 0.7F};
   }

   protected int[] get255Color() {
      return new int[]{120, 120, 120};
   }

   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      ItemStack itemStack = this.gui.getDropListEntry(var1);
      if (itemStack != null) {
         Minecraft.func_71410_x().field_71466_p.func_175065_a(itemStack.func_82833_r(), (float)(var2 + 2), (float)(var3 - 1), 0, false);
      }

   }
}

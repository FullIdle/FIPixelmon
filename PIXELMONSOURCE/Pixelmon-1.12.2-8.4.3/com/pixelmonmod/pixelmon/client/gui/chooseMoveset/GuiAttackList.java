package com.pixelmonmod.pixelmon.client.gui.chooseMoveset;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiSlotBase;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class GuiAttackList extends GuiSlotBase {
   private final Minecraft mc;
   private IElementClicked owner;
   private List list;

   public GuiAttackList(IElementClicked owner, List list, int width, int height, int top, int left, Minecraft mc) {
      super(top, left, width, height, true);
      this.owner = owner;
      this.list = list;
      this.mc = mc;
   }

   protected int getSize() {
      return this.list.size();
   }

   protected void elementClicked(int index, boolean doubleClicked) {
      this.owner.elementClicked(this.list, index);
   }

   protected boolean isSelected(int element) {
      return false;
   }

   protected void drawSlot(int index, int x, int yTop, int yMiddle, Tessellator tessellator) {
      if (index >= 0 && index < this.list.size()) {
         Attack a = (Attack)this.list.get(index);
         if (a != null) {
            this.mc.field_71466_p.func_78276_b(a.getMove().getLocalizedName(), x + 2, yTop - 1, 0);
         }

      }
   }

   protected float[] get1Color() {
      return new float[]{1.0F, 1.0F, 1.0F};
   }

   protected int[] getSelectionColor() {
      return new int[]{128, 128, 128};
   }
}

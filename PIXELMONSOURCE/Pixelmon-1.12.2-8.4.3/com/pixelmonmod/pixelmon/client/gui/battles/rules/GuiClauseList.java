package com.pixelmonmod.pixelmon.client.gui.battles.rules;

import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiSlotBase;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class GuiClauseList extends GuiSlotBase {
   private GuiBattleRulesBase parent;
   private List clauses;

   public GuiClauseList(GuiBattleRulesBase parent, List clauses, int top, int left, int width, int height) {
      super(top, left, width, height, true);
      this.parent = parent;
      this.clauses = clauses;
   }

   protected int getSize() {
      return this.clauses.size();
   }

   protected void elementClicked(int index, boolean doubleClicked) {
      this.parent.clauseListSelected(this.clauses, index);
   }

   protected boolean isSelected(int element) {
      return this.parent.isClauseSelected(this.clauses, element);
   }

   protected void drawSlot(int index, int x, int yTop, int yMiddle, Tessellator tessellator) {
      if (index >= 0 && index < this.clauses.size()) {
         Minecraft.func_71410_x().field_71466_p.func_78276_b(GuiHelper.getLimitedString(((BattleClause)this.clauses.get(index)).getLocalizedName(), this.width), x + 2, yTop - 1, 0);
      }
   }

   protected float[] get1Color() {
      return new float[]{1.0F, 1.0F, 1.0F};
   }

   protected int[] getSelectionColor() {
      return new int[]{128, 128, 128};
   }

   public BattleClause getElement(int index) {
      return (BattleClause)this.clauses.get(index);
   }
}

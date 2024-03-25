package com.pixelmonmod.tcg.gui;

import java.util.ArrayList;
import java.util.List;

public class GuiPlayerTargets {
   public GuiTarget activeTarget = new GuiTarget(0.0F);
   public GuiTarget trainerTarget = new GuiTarget(0.0F);
   public GuiTarget[] benchTargets = new GuiTarget[5];
   public List handTargets;

   public GuiPlayerTargets() {
      for(int i = 0; i < this.benchTargets.length; ++i) {
         this.benchTargets[i] = new GuiTarget(0.0F);
      }

      this.handTargets = new ArrayList();
   }

   public void draw(int mouseX, int mouseY) {
      this.activeTarget.draw(mouseX, mouseY);
      this.trainerTarget.draw(mouseX, mouseY);
      GuiTarget[] var3 = this.benchTargets;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         GuiTarget target = var3[var5];
         target.draw(mouseX, mouseY);
      }

      this.handTargets.forEach((c) -> {
         c.draw(mouseX, mouseY);
      });
   }
}

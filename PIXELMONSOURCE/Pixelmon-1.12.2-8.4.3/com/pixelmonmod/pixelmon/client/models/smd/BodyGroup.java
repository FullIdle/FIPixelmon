package com.pixelmonmod.pixelmon.client.models.smd;

import java.util.ArrayList;
import java.util.Iterator;

public class BodyGroup {
   public ArrayList models = new ArrayList();
   protected int currentModel;

   public void setAnimation(SmdAnimation anim) {
      Iterator var2 = this.models.iterator();

      while(var2.hasNext()) {
         SmdModel model = (SmdModel)var2.next();
         model.setAnimation(anim);
      }

   }

   public void render(boolean hasChanged, float partialTick) {
      if (this.currentModel >= 0) {
         ((SmdModel)this.models.get(this.currentModel)).render(hasChanged, partialTick);
      }

   }

   public void setActiveModel(int i) {
      if (i >= this.models.size()) {
         this.currentModel = -1;
      } else {
         this.currentModel = i;
      }

   }

   public SmdModel getActiveModel() {
      return this.currentModel >= 0 && this.currentModel < this.models.size() ? (SmdModel)this.models.get(this.currentModel) : null;
   }
}

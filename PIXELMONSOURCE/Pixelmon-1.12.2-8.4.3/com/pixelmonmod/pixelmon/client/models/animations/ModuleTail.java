package com.pixelmonmod.pixelmon.client.models.animations;

import java.util.ArrayList;
import net.minecraft.client.model.ModelRenderer;

public abstract class ModuleTail extends Module {
   public ArrayList tailParts;
   public int[] lengths;

   public ModuleTail(IModulized... tailArgs) {
      this.init((Object[])tailArgs);
   }

   public ModuleTail(ModelRenderer... tailArgs) {
      this.init((Object[])tailArgs);
   }

   private void init(Object... tailArgs) {
      this.tailParts = new ArrayList();
      Object[] var2 = tailArgs;
      int var3 = tailArgs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object m = var2[var4];
         this.tailParts.add(getModulizableFrom(m));
      }

      this.lengths = new int[this.tailParts.size()];
   }
}

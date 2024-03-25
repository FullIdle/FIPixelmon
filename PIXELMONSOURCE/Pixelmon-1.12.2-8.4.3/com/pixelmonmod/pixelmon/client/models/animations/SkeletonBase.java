package com.pixelmonmod.pixelmon.client.models.animations;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.model.ModelRenderer;

public class SkeletonBase {
   protected float toDegrees = 57.29578F;
   protected float toRadians;
   protected ArrayList modules;
   public ModelRenderer body;

   public SkeletonBase(ModelRenderer body) {
      this.toRadians = 1.0F / this.toDegrees;
      this.modules = new ArrayList();
      this.body = body;
   }

   public void add(Module module) {
      this.modules.add(module);
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      Iterator var7 = this.modules.iterator();

      while(var7.hasNext()) {
         Module m = (Module)var7.next();
         m.walk(entity, f, f1, f2, f3, f4);
      }

   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      Iterator var7 = this.modules.iterator();

      while(var7.hasNext()) {
         Module m = (Module)var7.next();
         m.fly(entity, f, f1, f2, f3, f4);
      }

   }

   public void swim(Entity2Client pixelmon, float f, float f1, float f2, float f3, float f4) {
   }
}

package com.pixelmonmod.pixelmon.client.models.animations;

import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import java.util.ArrayList;
import net.minecraft.client.model.ModelRenderer;

public abstract class Module {
   public static final float toDegrees = 57.29578F;
   public static final float toRadians = 0.017453292F;
   protected ArrayList modules = new ArrayList();
   protected float xrD;
   protected float yrD;
   protected float zrD;

   public abstract void walk(Entity2Client var1, float var2, float var3, float var4, float var5, float var6);

   public abstract void fly(Entity2Client var1, float var2, float var3, float var4, float var5, float var6);

   public float getDelta(EnumRotation rot) {
      switch (rot) {
         case x:
            return this.xrD;
         case y:
            return this.yrD;
         case z:
            return this.zrD;
         default:
            return 0.0F;
      }
   }

   public static IModulized getModulizableFrom(Object obj) {
      if (obj instanceof ModelRenderer) {
         return new ModulizedRenderWrapper((ModelRenderer)obj);
      } else if (obj instanceof IModulized) {
         return (IModulized)obj;
      } else {
         throw new IllegalArgumentException("The first variable passed-in to the Module contstructor must either be a ModelRenderer, or an instance of IModulizable. The class of the passed-in arm variable was " + obj.getClass().getSimpleName());
      }
   }

   public Module addModule(Module m) {
      this.modules.add(m);
      return this;
   }
}

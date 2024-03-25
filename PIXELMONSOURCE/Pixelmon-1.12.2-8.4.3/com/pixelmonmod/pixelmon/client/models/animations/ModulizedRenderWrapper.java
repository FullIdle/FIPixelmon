package com.pixelmonmod.pixelmon.client.models.animations;

import net.minecraft.client.model.ModelRenderer;

public class ModulizedRenderWrapper implements IModulized {
   ModelRenderer renderer;
   float x;
   float y;
   float z;
   float xr;
   float yr;
   float zr;
   float x0;
   float y0;
   float z0;
   float xr0;
   float yr0;
   float zr0;

   public ModulizedRenderWrapper(ModelRenderer renderer) {
      this.renderer = renderer;
   }

   public float getValue(EnumGeomData d) {
      switch (d) {
         case xloc:
            return -this.renderer.field_78800_c;
         case yloc:
            return -this.renderer.field_78797_d;
         case zloc:
            return -this.renderer.field_78798_e;
         case xrot:
            return this.renderer.field_78795_f;
         case yrot:
            return this.renderer.field_78796_g;
         case zrot:
            return this.renderer.field_78808_h;
         default:
            return -1.0F;
      }
   }

   public float setValue(float value, EnumGeomData d) {
      switch (d) {
         case xloc:
            this.renderer.field_78800_c = -value;
            break;
         case yloc:
            this.renderer.field_78797_d = -value;
            break;
         case zloc:
            this.renderer.field_78797_d = -value;
            break;
         case xrot:
            this.renderer.field_78795_f = value;
            break;
         case yrot:
            this.renderer.field_78796_g = value;
            break;
         case zrot:
            this.renderer.field_78808_h = value;
      }

      return value;
   }
}

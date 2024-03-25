package com.pixelmonmod.pixelmon.client.models.discs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelDiscHemiSphere extends ModelBase {
   ModelRenderer RedTip;
   ModelRenderer RedTop;
   ModelRenderer RedFront;
   ModelRenderer RedBottom;
   ModelRenderer RedRight;
   ModelRenderer RedLeft;
   ModelRenderer RedBack;

   public ModelDiscHemiSphere() {
      this.field_78090_t = 32;
      this.field_78089_u = 32;
      this.RedTip = new ModelRenderer(this, 18, 0);
      this.RedTip.func_78789_a(-1.0F, -1.2F, -1.0F, 2, 1, 2);
      this.RedTip.func_78793_a(6.5F, 13.0F, 0.0F);
      this.RedTip.func_78787_b(32, 32);
      this.RedTip.field_78809_i = true;
      this.setRotation(this.RedTip, 0.0F, 0.0F, 0.0F);
      this.RedTop = new ModelRenderer(this, 0, 0);
      this.RedTop.func_78789_a(-1.5F, -0.8F, -1.5F, 3, 1, 3);
      this.RedTop.func_78793_a(6.5F, 13.0F, 0.0F);
      this.RedTop.func_78787_b(32, 32);
      this.RedTop.field_78809_i = true;
      this.setRotation(this.RedTop, 0.0F, 0.0F, 0.0F);
      this.RedFront = new ModelRenderer(this, 12, 0);
      this.RedFront.func_78789_a(-1.0F, -0.3F, -1.9F, 2, 1, 1);
      this.RedFront.func_78793_a(6.5F, 13.0F, 0.0F);
      this.RedFront.func_78787_b(32, 32);
      this.RedFront.field_78809_i = true;
      this.setRotation(this.RedFront, 0.0F, 0.0F, 0.0F);
      this.RedBottom = new ModelRenderer(this, 0, 4);
      this.RedBottom.func_78789_a(-1.5F, -0.3F, -1.5F, 3, 1, 3);
      this.RedBottom.func_78793_a(6.5F, 13.0F, 0.0F);
      this.RedBottom.func_78787_b(32, 32);
      this.RedBottom.field_78809_i = true;
      this.setRotation(this.RedBottom, 0.0F, 0.0F, 0.0F);
      this.RedRight = new ModelRenderer(this, 0, 8);
      this.RedRight.func_78789_a(-1.9F, -0.3F, -1.0F, 1, 1, 2);
      this.RedRight.func_78793_a(6.5F, 13.0F, 0.0F);
      this.RedRight.func_78787_b(32, 32);
      this.RedRight.field_78809_i = true;
      this.setRotation(this.RedRight, 0.0F, 0.0F, 0.0F);
      this.RedLeft = new ModelRenderer(this, 6, 8);
      this.RedLeft.func_78789_a(0.9F, -0.3F, -1.0F, 1, 1, 2);
      this.RedLeft.func_78793_a(6.5F, 13.0F, 0.0F);
      this.RedLeft.func_78787_b(32, 32);
      this.RedLeft.field_78809_i = true;
      this.setRotation(this.RedLeft, 0.0F, 0.0F, 0.0F);
      this.RedBack = new ModelRenderer(this, 12, 2);
      this.RedBack.func_78789_a(-1.0F, -0.3F, 0.9F, 2, 1, 1);
      this.RedBack.func_78793_a(6.5F, 13.0F, 0.0F);
      this.RedBack.func_78787_b(32, 32);
      this.RedBack.field_78809_i = true;
      this.setRotation(this.RedBack, 0.0F, 0.0F, 0.0F);
   }

   public void renderModel(float f5) {
      this.RedTip.func_78785_a(f5);
      this.RedTop.func_78785_a(f5);
      this.RedFront.func_78785_a(f5);
      this.RedBottom.func_78785_a(f5);
      this.RedRight.func_78785_a(f5);
      this.RedLeft.func_78785_a(f5);
      this.RedBack.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}

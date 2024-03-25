package com.pixelmonmod.pixelmon.client.models.plates;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPlateStage3 extends ModelBase {
   ModelRenderer Ingot1;
   ModelRenderer Ingot2;
   ModelRenderer Ingot3;
   ModelRenderer Ingot4;

   public ModelPlateStage3() {
      this.field_78090_t = 32;
      this.field_78089_u = 32;
      this.Ingot1 = new ModelRenderer(this, 0, 0);
      this.Ingot1.func_78789_a(-13.9F, 0.9F, -1.5F, 5, 1, 3);
      this.Ingot1.func_78793_a(6.5F, 12.0F, 0.0F);
      this.Ingot1.func_78787_b(32, 32);
      this.Ingot1.field_78809_i = true;
      this.setRotation(this.Ingot1, 0.0F, 0.0F, 0.0F);
      this.Ingot2 = new ModelRenderer(this, 0, 9);
      this.Ingot2.func_78789_a(-9.4F, 0.0F, -2.0F, 1, 1, 4);
      this.Ingot2.func_78793_a(6.5F, 13.0F, 0.0F);
      this.Ingot2.func_78787_b(32, 32);
      this.Ingot2.field_78809_i = true;
      this.setRotation(this.Ingot2, 0.0F, 0.0F, 0.0F);
      this.Ingot3 = new ModelRenderer(this, 0, 4);
      this.Ingot3.func_78789_a(-13.9F, 0.0F, -2.5F, 5, 1, 5);
      this.Ingot3.func_78793_a(6.5F, 13.0F, 0.0F);
      this.Ingot3.func_78787_b(32, 32);
      this.Ingot3.field_78809_i = true;
      this.setRotation(this.Ingot3, 0.0F, 0.0F, 0.0F);
      this.Ingot4 = new ModelRenderer(this, 0, 9);
      this.Ingot4.func_78789_a(-14.4F, 0.0F, -2.0F, 1, 1, 4);
      this.Ingot4.func_78793_a(6.5F, 13.0F, 0.0F);
      this.Ingot4.func_78787_b(32, 32);
      this.Ingot4.field_78809_i = true;
      this.setRotation(this.Ingot4, 0.0F, 0.0F, 0.0F);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      this.Ingot1.func_78785_a(f5);
      this.Ingot2.func_78785_a(f5);
      this.Ingot3.func_78785_a(f5);
      this.Ingot4.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }

   public void renderModel(float f) {
      this.Ingot1.func_78785_a(f);
      this.Ingot2.func_78785_a(f);
      this.Ingot3.func_78785_a(f);
      this.Ingot4.func_78785_a(f);
   }
}

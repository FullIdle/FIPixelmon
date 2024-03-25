package com.pixelmonmod.pixelmon.client.models.plates;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPlate extends ModelBase {
   ModelRenderer Plate;

   public ModelPlate() {
      this.field_78090_t = 32;
      this.field_78089_u = 32;
      this.Plate = new ModelRenderer(this, 3, 20);
      this.Plate.func_78789_a(-13.9F, 0.0F, -2.5F, 6, 1, 6);
      this.Plate.func_78793_a(6.5F, 13.0F, 0.0F);
      this.Plate.func_78787_b(32, 32);
      this.Plate.field_78809_i = true;
      this.setRotation(this.Plate, 0.0F, 0.0F, 0.0F);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      this.Plate.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }

   public void renderModel(float f5) {
      this.Plate.func_78785_a(f5);
   }
}

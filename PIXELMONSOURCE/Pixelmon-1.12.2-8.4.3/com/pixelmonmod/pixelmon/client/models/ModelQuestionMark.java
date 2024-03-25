package com.pixelmonmod.pixelmon.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelQuestionMark extends ModelBase {
   ModelRenderer MiddleDot;
   ModelRenderer MiddleBase;
   ModelRenderer MiddleOuter;
   ModelRenderer MiddleTop;
   ModelRenderer MiddleHook;

   public ModelQuestionMark() {
      this.field_78090_t = 64;
      this.field_78089_u = 32;
      this.MiddleDot = new ModelRenderer(this, 0, 0);
      this.MiddleDot.func_78789_a(-2.0F, -2.0F, -2.0F, 4, 4, 4);
      this.MiddleDot.func_78793_a(0.0F, 20.0F, 0.0F);
      this.MiddleDot.func_78787_b(64, 32);
      this.MiddleDot.field_78809_i = true;
      this.setRotation(this.MiddleDot, 0.0F, 0.0F, 0.0F);
      this.MiddleBase = new ModelRenderer(this, 0, 0);
      this.MiddleBase.func_78789_a(-2.0F, -3.0F, -2.0F, 4, 6, 4);
      this.MiddleBase.func_78793_a(0.0F, 14.0F, 0.0F);
      this.MiddleBase.func_78787_b(64, 32);
      this.MiddleBase.field_78809_i = true;
      this.setRotation(this.MiddleBase, 0.0F, 0.0F, 0.0F);
      this.MiddleOuter = new ModelRenderer(this, 0, 0);
      this.MiddleOuter.func_78789_a(-2.0F, -4.0F, -2.0F, 4, 8, 4);
      this.MiddleOuter.func_78793_a(4.0F, 7.0F, 0.0F);
      this.MiddleOuter.func_78787_b(64, 32);
      this.MiddleOuter.field_78809_i = true;
      this.setRotation(this.MiddleOuter, 0.0F, 0.0F, 0.0F);
      this.MiddleTop = new ModelRenderer(this, 0, 0);
      this.MiddleTop.func_78789_a(-2.0F, -2.0F, -2.0F, 4, 4, 4);
      this.MiddleTop.func_78793_a(0.0F, 1.0F, 0.0F);
      this.MiddleTop.func_78787_b(64, 32);
      this.MiddleTop.field_78809_i = true;
      this.setRotation(this.MiddleTop, 0.0F, 0.0F, 0.0F);
      this.MiddleHook = new ModelRenderer(this, 0, 0);
      this.MiddleHook.func_78789_a(-2.0F, -2.0F, -2.0F, 4, 4, 4);
      this.MiddleHook.func_78793_a(-4.0F, 5.0F, 0.0F);
      this.MiddleHook.func_78787_b(64, 32);
      this.MiddleHook.field_78809_i = true;
      this.setRotation(this.MiddleHook, 0.0F, 0.0F, 0.0F);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      this.MiddleDot.func_78785_a(f5);
      this.MiddleBase.func_78785_a(f5);
      this.MiddleOuter.func_78785_a(f5);
      this.MiddleTop.func_78785_a(f5);
      this.MiddleHook.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}

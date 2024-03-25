package com.pixelmonmod.pixelmon.client.models.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTV extends ModelBase {
   ModelRenderer FrontLeft;
   ModelRenderer SideRightTop;
   ModelRenderer SideLeft;
   ModelRenderer Front;
   ModelRenderer FrontRight;
   ModelRenderer Top1;
   ModelRenderer BaseRight;
   ModelRenderer Back1;
   ModelRenderer FrontTop;
   ModelRenderer Top2;
   ModelRenderer BaseLeft;
   ModelRenderer SideLeftTop;
   ModelRenderer Screen;
   ModelRenderer SideRight;
   ModelRenderer Back2;

   public ModelTV() {
      this.field_78090_t = 64;
      this.field_78089_u = 64;
      this.FrontLeft = new ModelRenderer(this, 32, 2);
      this.FrontLeft.func_78789_a(0.0F, 0.0F, 0.0F, 3, 7, 6);
      this.FrontLeft.func_78793_a(3.5F, 17.0F, -6.0F);
      this.FrontLeft.func_78787_b(64, 64);
      this.FrontLeft.field_78809_i = true;
      this.setRotation(this.FrontLeft, 0.0F, -1.003822F, 0.0F);
      this.SideRightTop = new ModelRenderer(this, 0, 0);
      this.SideRightTop.func_78789_a(0.0F, -2.0F, 0.0F, 2, 2, 9);
      this.SideRightTop.func_78793_a(-5.7F, 7.0F, -3.7F);
      this.SideRightTop.func_78787_b(64, 64);
      this.SideRightTop.field_78809_i = true;
      this.setRotation(this.SideRightTop, 0.0F, 0.0F, 0.7243116F);
      this.SideLeft = new ModelRenderer(this, 0, 0);
      this.SideLeft.func_78789_a(-2.0F, 0.0F, 0.0F, 2, 7, 9);
      this.SideLeft.func_78793_a(5.7F, 7.0F, -3.7F);
      this.SideLeft.func_78787_b(64, 64);
      this.SideLeft.field_78809_i = true;
      this.setRotation(this.SideLeft, 0.0F, 0.0F, 0.0F);
      this.Front = new ModelRenderer(this, 38, 19);
      this.Front.func_78789_a(0.0F, 0.0F, 0.0F, 7, 7, 4);
      this.Front.func_78793_a(-3.5F, 17.0F, -6.0F);
      this.Front.func_78787_b(64, 64);
      this.Front.field_78809_i = true;
      this.setRotation(this.Front, 0.0F, 0.0F, 0.0F);
      this.FrontRight = new ModelRenderer(this, 34, 2);
      this.FrontRight.func_78789_a(-3.0F, 0.0F, 0.0F, 3, 7, 6);
      this.FrontRight.func_78793_a(-3.5F, 17.0F, -6.0F);
      this.FrontRight.func_78787_b(64, 64);
      this.FrontRight.field_78809_i = true;
      this.setRotation(this.FrontRight, 0.0F, 1.003822F, 0.0F);
      this.Top1 = new ModelRenderer(this, 0, 0);
      this.Top1.func_78789_a(0.0F, -2.0F, 0.0F, 4, 2, 9);
      this.Top1.func_78793_a(-4.4F, 7.5F, -3.7F);
      this.Top1.func_78787_b(64, 64);
      this.Top1.field_78809_i = true;
      this.setRotation(this.Top1, 0.0F, 0.0F, 0.0F);
      this.BaseRight = new ModelRenderer(this, 0, 45);
      this.BaseRight.func_78789_a(-4.0F, 0.0F, 0.0F, 11, 10, 9);
      this.BaseRight.func_78793_a(-1.7F, 14.0F, -3.7F);
      this.BaseRight.func_78787_b(64, 64);
      this.BaseRight.field_78809_i = true;
      this.setRotation(this.BaseRight, 0.0F, 0.0F, 0.0F);
      this.Back1 = new ModelRenderer(this, 0, 0);
      this.Back1.func_78789_a(-8.0F, 0.0F, 0.0F, 8, 8, 1);
      this.Back1.func_78793_a(3.7F, 7.0F, 5.5F);
      this.Back1.func_78787_b(64, 64);
      this.Back1.field_78809_i = true;
      this.setRotation(this.Back1, 0.0F, 0.0F, 0.0F);
      this.FrontTop = new ModelRenderer(this, 0, 0);
      this.FrontTop.func_78789_a(-3.5F, 0.0F, -2.0F, 7, 2, 3);
      this.FrontTop.func_78793_a(0.0F, 16.0F, -3.5F);
      this.FrontTop.func_78787_b(64, 64);
      this.FrontTop.field_78809_i = true;
      this.setRotation(this.FrontTop, 0.5948606F, 0.0F, 0.0F);
      this.Top2 = new ModelRenderer(this, 0, 0);
      this.Top2.func_78789_a(-5.0F, -2.0F, 0.0F, 5, 2, 9);
      this.Top2.func_78793_a(4.4F, 7.5F, -3.7F);
      this.Top2.func_78787_b(64, 64);
      this.Top2.field_78809_i = true;
      this.setRotation(this.Top2, 0.0F, 0.0F, 0.0F);
      this.BaseLeft = new ModelRenderer(this, 0, 45);
      this.BaseLeft.func_78789_a(-4.0F, 0.0F, 0.0F, 11, 10, 9);
      this.BaseLeft.func_78793_a(-1.3F, 14.0F, -3.7F);
      this.BaseLeft.func_78787_b(64, 64);
      this.BaseLeft.field_78809_i = true;
      this.setRotation(this.BaseLeft, 0.0F, 0.0F, 0.0F);
      this.SideLeftTop = new ModelRenderer(this, 0, 0);
      this.SideLeftTop.func_78789_a(-2.0F, -2.0F, 0.0F, 2, 2, 9);
      this.SideLeftTop.func_78793_a(5.7F, 7.0F, -3.7F);
      this.SideLeftTop.func_78787_b(64, 64);
      this.SideLeftTop.field_78809_i = true;
      this.setRotation(this.SideLeftTop, 0.0F, 0.0F, -0.7243116F);
      this.Screen = new ModelRenderer(this, 0, 23);
      this.Screen.func_78789_a(-8.0F, 0.0F, 0.0F, 8, 8, 9);
      this.Screen.func_78793_a(3.7F, 7.0F, -3.5F);
      this.Screen.func_78787_b(64, 64);
      this.Screen.field_78809_i = true;
      this.setRotation(this.Screen, 0.0F, 0.0F, 0.0F);
      this.SideRight = new ModelRenderer(this, 0, 0);
      this.SideRight.func_78789_a(-2.0F, 0.0F, 0.0F, 2, 7, 9);
      this.SideRight.func_78793_a(-3.7F, 7.0F, -3.7F);
      this.SideRight.func_78787_b(64, 64);
      this.SideRight.field_78809_i = true;
      this.setRotation(this.SideRight, 0.0F, 0.0F, 0.0F);
      this.Back2 = new ModelRenderer(this, 0, 0);
      this.Back2.func_78789_a(-8.0F, 0.0F, 0.0F, 7, 7, 2);
      this.Back2.func_78793_a(4.2F, 7.5F, 5.5F);
      this.Back2.func_78787_b(64, 64);
      this.Back2.field_78809_i = true;
      this.setRotation(this.Back2, 0.0F, 0.0F, 0.0F);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      this.FrontLeft.func_78785_a(f5);
      this.SideRightTop.func_78785_a(f5);
      this.SideLeft.func_78785_a(f5);
      this.Front.func_78785_a(f5);
      this.FrontRight.func_78785_a(f5);
      this.Top1.func_78785_a(f5);
      this.BaseRight.func_78785_a(f5);
      this.Back1.func_78785_a(f5);
      this.FrontTop.func_78785_a(f5);
      this.Top2.func_78785_a(f5);
      this.BaseLeft.func_78785_a(f5);
      this.SideLeftTop.func_78785_a(f5);
      this.Screen.func_78785_a(f5);
      this.SideRight.func_78785_a(f5);
      this.Back2.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }

   public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.func_78087_a(f, f1, f2, f3, f4, f5, entity);
   }
}

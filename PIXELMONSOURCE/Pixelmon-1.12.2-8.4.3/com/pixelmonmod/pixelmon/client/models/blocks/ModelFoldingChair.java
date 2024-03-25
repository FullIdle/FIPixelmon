package com.pixelmonmod.pixelmon.client.models.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFoldingChair extends ModelBase {
   ModelRenderer SeatSide1;
   ModelRenderer Leg1;
   ModelRenderer Leg2;
   ModelRenderer Arm1;
   ModelRenderer Leg3;
   ModelRenderer Arm2;
   ModelRenderer MainSeat;
   ModelRenderer SeatSide2;
   ModelRenderer Leg4;

   public ModelFoldingChair() {
      this.field_78090_t = 64;
      this.field_78089_u = 64;
      this.SeatSide1 = new ModelRenderer(this, 0, 18);
      this.SeatSide1.func_78789_a(0.0F, 0.0F, 0.0F, 4, 2, 16);
      this.SeatSide1.func_78793_a(8.0F, 9.0F, -8.0F);
      this.SeatSide1.func_78787_b(64, 64);
      this.SeatSide1.field_78809_i = true;
      this.setRotation(this.SeatSide1, 0.0F, 0.0F, 1.003822F);
      this.Leg1 = new ModelRenderer(this, 0, 36);
      this.Leg1.func_78789_a(0.0F, -20.0F, 0.0F, 2, 20, 2);
      this.Leg1.func_78793_a(-8.0F, 23.0F, 6.0F);
      this.Leg1.func_78787_b(64, 64);
      this.Leg1.field_78809_i = true;
      this.setRotation(this.Leg1, 0.0F, 0.0F, 0.8179294F);
      this.Leg2 = new ModelRenderer(this, 0, 36);
      this.Leg2.func_78789_a(-2.0F, -20.0F, 0.0F, 2, 20, 2);
      this.Leg2.func_78793_a(8.0F, 23.0F, -8.0F);
      this.Leg2.func_78787_b(64, 64);
      this.Leg2.field_78809_i = true;
      this.setRotation(this.Leg2, 0.0F, 0.0F, -0.8179294F);
      this.Arm1 = new ModelRenderer(this, 9, 37);
      this.Arm1.func_78789_a(0.0F, 0.0F, 0.0F, 2, 2, 16);
      this.Arm1.func_78793_a(6.0F, 22.0F, -8.0F);
      this.Arm1.func_78787_b(64, 64);
      this.Arm1.field_78809_i = true;
      this.setRotation(this.Arm1, 0.0F, 0.0F, 0.0F);
      this.Leg3 = new ModelRenderer(this, 0, 36);
      this.Leg3.func_78789_a(-2.0F, -20.0F, 0.0F, 2, 20, 2);
      this.Leg3.func_78793_a(8.0F, 23.0F, 6.0F);
      this.Leg3.func_78787_b(64, 64);
      this.Leg3.field_78809_i = true;
      this.setRotation(this.Leg3, 0.0F, 0.0F, -0.8179294F);
      this.Arm2 = new ModelRenderer(this, 9, 37);
      this.Arm2.func_78789_a(0.0F, 0.0F, 0.0F, 2, 2, 16);
      this.Arm2.func_78793_a(-8.0F, 22.0F, -8.0F);
      this.Arm2.func_78787_b(64, 64);
      this.Arm2.field_78809_i = true;
      this.setRotation(this.Arm2, 0.0F, 0.0F, 0.0F);
      this.MainSeat = new ModelRenderer(this, 0, 0);
      this.MainSeat.func_78789_a(0.0F, 0.0F, 0.0F, 16, 2, 16);
      this.MainSeat.func_78793_a(-8.0F, 9.0F, -8.0F);
      this.MainSeat.func_78787_b(64, 64);
      this.MainSeat.field_78809_i = true;
      this.setRotation(this.MainSeat, 0.0F, 0.0F, 0.0F);
      this.SeatSide2 = new ModelRenderer(this, 0, 18);
      this.SeatSide2.func_78789_a(-4.0F, 0.0F, 0.0F, 4, 2, 16);
      this.SeatSide2.func_78793_a(-8.0F, 9.0F, -8.0F);
      this.SeatSide2.func_78787_b(64, 64);
      this.SeatSide2.field_78809_i = true;
      this.setRotation(this.SeatSide2, 0.0F, 0.0F, -1.003822F);
      this.Leg4 = new ModelRenderer(this, 0, 36);
      this.Leg4.func_78789_a(0.0F, -20.0F, 0.0F, 2, 20, 2);
      this.Leg4.func_78793_a(-8.0F, 23.0F, -8.0F);
      this.Leg4.func_78787_b(64, 64);
      this.Leg4.field_78809_i = true;
      this.setRotation(this.Leg4, 0.0F, 0.0F, 0.8179294F);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      this.SeatSide1.func_78785_a(f5);
      this.Leg1.func_78785_a(f5);
      this.Leg2.func_78785_a(f5);
      this.Arm1.func_78785_a(f5);
      this.Leg3.func_78785_a(f5);
      this.Arm2.func_78785_a(f5);
      this.MainSeat.func_78785_a(f5);
      this.SeatSide2.func_78785_a(f5);
      this.Leg4.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}

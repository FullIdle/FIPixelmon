package com.pixelmonmod.pixelmon.client.models.animations.bird;

import com.pixelmonmod.pixelmon.client.models.animations.EnumGeomData;
import com.pixelmonmod.pixelmon.client.models.animations.IModulized;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.AnimationVariables;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModuleWingComplex extends ModuleWing {
   AnimationVariables animationVariables;

   public ModuleWingComplex(ModelRenderer wing, EnumWing WingVariable, float WingOrientation, float WingRotationLimit) {
      super(wing, WingVariable, WingOrientation, WingRotationLimit, 0.0F);
      this.registerAnimationCounters();
   }

   public ModuleWingComplex(IModulized wing, EnumWing WingVariable, float WingOrientation, float WingRotationLimit) {
      super(wing, WingVariable, WingOrientation, WingRotationLimit, 0.0F);
      this.registerAnimationCounters();
   }

   public void walk(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
      IncrementingVariable counter = this.getCounter(0, entity);
      if (counter == null) {
         this.setCounter(0, 90, 6, entity);
         counter = this.getCounter(0, entity);
      }

      if (this.WingVariable == EnumWing.Left) {
         if (counter.value < 45.0F) {
            this.yrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.WingOrientation)) * ((float)(1.0090999603271484 - 1.0089999437332153 * Math.exp(-Math.toRadians((double)counter.value) * 6.0)) * 3.1415927F * 2.0F * this.WingRotationLimit - 3.1415927F * this.WingRotationLimit);
            this.zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.WingOrientation)) * ((float)(1.0090999603271484 - 1.0089999437332153 * Math.exp(-Math.toRadians((double)counter.value) * 6.0)) * 3.1415927F * 2.0F * this.WingRotationLimit - 3.1415927F * this.WingRotationLimit);
         } else {
            this.yrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.WingOrientation)) * (float)(0.5 - 0.5 * Math.cos(Math.toRadians((double)counter.value) * 4.0) * 3.1415927410125732 * 2.0 * (double)this.WingRotationLimit - (double)(3.1415927F * this.WingRotationLimit));
            this.zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.WingOrientation)) * (float)(0.5 - 0.5 * Math.cos(Math.toRadians((double)counter.value) * 4.0) * 3.1415927410125732 * 2.0 * (double)this.WingRotationLimit - (double)(3.1415927F * this.WingRotationLimit));
         }
      } else if (counter.value < 45.0F) {
         this.yrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.WingOrientation)) * ((float)Math.exp(-Math.toRadians((double)counter.value) * 6.0) * 3.1415927F * 2.0F * this.WingRotationLimit - 3.1415927F * this.WingRotationLimit);
         this.zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.WingOrientation)) * ((float)Math.exp(-Math.toRadians((double)counter.value) * 6.0) * 3.1415927F * 2.0F * this.WingRotationLimit - 3.1415927F * this.WingRotationLimit);
      } else {
         this.yrD = MathHelper.func_76134_b((float)Math.toRadians((double)this.WingOrientation)) * (float)((0.5 * Math.cos(Math.toRadians((double)counter.value) * 4.0) - 0.5) * 3.1415927410125732 * 2.0 * (double)this.WingRotationLimit + (double)(3.1415927F * this.WingRotationLimit));
         this.zrD = MathHelper.func_76126_a((float)Math.toRadians((double)this.WingOrientation)) * ((float)(0.5 * Math.cos(Math.toRadians((double)counter.value) * 4.0) - 0.5) * 3.1415927F * 2.0F * this.WingRotationLimit + 3.1415927F * this.WingRotationLimit);
      }

      this.wing.setValue(this.yrD, EnumGeomData.yrot);
      this.wing.setValue(this.zrD, EnumGeomData.zrot);
   }

   public void fly(Entity2Client entity, float f, float f1, float f2, float f3, float f4) {
   }

   protected void registerAnimationCounters() {
   }

   protected void setInt(int id, int value, EntityPixelmon pixelmon) {
      pixelmon.getAnimationVariables().setInt(id, value);
   }

   protected int getInt(int id, EntityPixelmon pixelmon) {
      return pixelmon.getAnimationVariables().getInt(id);
   }

   protected void setCounter(int id, int limit, int increment, Entity2Client pixelmon) {
      pixelmon.getAnimationVariables().setCounter(id, (float)limit, (float)increment);
   }

   protected IncrementingVariable getCounter(int id, Entity2Client pixelmon) {
      return pixelmon.getAnimationVariables().getCounter(id);
   }
}

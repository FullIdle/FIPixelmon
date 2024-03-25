package com.pixelmonmod.pixelmon.client.models;

import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public abstract class PixelmonModelBase extends ModelBase {
   public float scale = 1.0F;
   protected SkeletonBase skeleton;
   public float movementThreshold = 0.3F;

   public void func_78088_a(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (entityIn instanceof EntityPixelmon && this.skeleton != null) {
         this.doAnimation(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      }

   }

   public void doAnimation(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      Entity3HasStats pixelmon = (Entity3HasStats)entity;
      if (entity.func_70090_H()) {
         this.skeleton.swim(pixelmon, f, f1, f2, f3, f4);
      } else if (entity.field_70160_al) {
         this.skeleton.fly(pixelmon, f, f1, f2, f3, f4);
      } else {
         this.skeleton.walk(pixelmon, f, f1, f2, f3, f4);
      }

   }

   public void setRotations(EntityPixelmon entity) {
      if (this.skeleton != null && this.skeleton.body != null) {
         if (!entity.getStaysHorizontalInRender() && entity.getIsFlying()) {
            this.skeleton.body.field_78808_h = 0.017453292F * entity.rotationRoll;
            this.skeleton.body.field_78795_f = this.getDefaultXRotation() - 0.017453292F * entity.field_70125_A;
         } else {
            this.skeleton.body.field_78808_h = 0.0F;
            this.skeleton.body.field_78795_f = this.getDefaultXRotation();
         }
      }

   }

   protected float getDefaultXRotation() {
      return -1.5707964F;
   }

   public void func_78086_a(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
      if (entity instanceof EntityPixelmon && this instanceof PixelmonSmdFactory.Impl) {
         this.setRotations((EntityPixelmon)entity);
      }

      super.func_78086_a(entity, limbSwing, limbSwingAmount, partialTickTime);
   }

   public float getScale() {
      return this.scale;
   }

   protected void setInt(int id, int value, Entity2Client pixelmon) {
      pixelmon.getAnimationVariables().setInt(id, value);
   }

   protected int getInt(int id, Entity2Client pixelmon) {
      return pixelmon.getAnimationVariables().getInt(id);
   }

   protected IncrementingVariable setCounter(int id, int limit, int increment, Entity2Client pixelmon) {
      pixelmon.getAnimationVariables().setCounter(id, (float)limit, (float)increment);
      return this.getCounter(id, pixelmon);
   }

   protected IncrementingVariable getCounter(int id, Entity2Client pixelmon) {
      return pixelmon.getAnimationVariables().getCounter(id);
   }

   protected void registerAnimationCounters() {
   }

   protected boolean hasInt(int id, Entity2Client pixelmon) {
      return pixelmon.getAnimationVariables().hasInt(id);
   }
}

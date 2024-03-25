package com.pixelmonmod.pixelmon.client.models;

import com.pixelmonmod.pixelmon.client.models.animations.SkeletonBase;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModelLoader;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class PixelmonSmdFactory {
   protected ResourceLocation model;
   protected float xRotation;
   protected float yRotation = 24.0F;
   protected float zRotation;
   protected float rotateAngleX = -1.5707964F;
   protected float rotateAngleY;
   protected float movementThreshold = 0.3F;
   protected float animationIncrement = 1.0F;
   protected float xOffset;
   protected float yOffset;
   protected float zOffset;
   protected float transparency = 0.0F;

   public PixelmonSmdFactory(EnumPokemonModel model) {
      this.model = model.getModelLocation();
   }

   public PixelmonSmdFactory(ResourceLocation model) {
      this.model = model;
   }

   public PixelmonSmdFactory setXRotation(float xRotation) {
      this.xRotation = xRotation;
      return this;
   }

   public PixelmonSmdFactory setYRotation(float yRotation) {
      this.yRotation = yRotation;
      return this;
   }

   public PixelmonSmdFactory setZRotation(float zRotation) {
      this.zRotation = zRotation;
      return this;
   }

   public PixelmonSmdFactory setTranslation(float translation) {
      this.xRotation = translation;
      this.zRotation = translation;
      return this;
   }

   public PixelmonSmdFactory setMovementThreshold(float threshold) {
      this.movementThreshold = threshold;
      return this;
   }

   public PixelmonSmdFactory setRotateAngleX(float rotateAngleX) {
      this.rotateAngleX = rotateAngleX;
      return this;
   }

   public PixelmonSmdFactory setRotateAngleY(float rotateAngleY) {
      this.rotateAngleY = rotateAngleY;
      return this;
   }

   public PixelmonSmdFactory setAnimationIncrement(float animationIncrement) {
      this.animationIncrement = animationIncrement;
      return this;
   }

   public PixelmonSmdFactory setTransparency(float transparency) {
      this.transparency = transparency;
      return this;
   }

   public PixelmonSmdFactory setXOffset(float xOffset) {
      this.xOffset = xOffset;
      return this;
   }

   public PixelmonSmdFactory setYOffset(float yOffset) {
      this.yOffset = yOffset;
      return this;
   }

   public PixelmonSmdFactory setZOffset(float zOffset) {
      this.zOffset = zOffset;
      return this;
   }

   protected PixelmonModelSmd createModel() {
      Impl impl = new Impl(this, loadModel(this.model));
      impl.body.func_78793_a(this.xRotation, this.yRotation, this.zRotation);
      impl.body.field_78795_f = this.rotateAngleX;
      impl.body.field_78796_g = this.rotateAngleY;
      impl.body.field_82906_o = this.xOffset;
      impl.body.field_82908_p = this.yOffset;
      impl.body.field_82907_q = this.zOffset;
      impl.movementThreshold = this.movementThreshold;
      impl.animationIncrement = this.animationIncrement;
      impl.body.setTransparent(this.transparency);
      return impl;
   }

   protected static ValveStudioModel loadModel(ResourceLocation rl) {
      try {
         if (ValveStudioModelLoader.instance.accepts(rl)) {
            return (ValveStudioModel)ValveStudioModelLoader.instance.loadModel(rl);
         }

         System.out.println("Could not load the smd model: " + rl.func_110623_a());
      } catch (Exception var2) {
         System.out.println("Could not load the smd model: " + rl.func_110623_a());
         var2.printStackTrace();
      }

      return null;
   }

   public static class Impl extends PixelmonModelSmd {
      protected final PixelmonSmdFactory factory;
      protected PixelmonModelRenderer body;

      Impl(PixelmonSmdFactory factory, ValveStudioModel valveStudioModel) {
         this.theModel = valveStudioModel;
         this.factory = factory;
         this.body = new PixelmonModelRenderer(this, "body");
         this.body.addCustomModel(new ModelCustomWrapper(valveStudioModel));
         this.skeleton = new SkeletonBase(this.body);
      }

      public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
         super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
         this.body.render(f5, this.partialTick);
      }

      protected float getDefaultXRotation() {
         return this.factory.rotateAngleX;
      }
   }
}

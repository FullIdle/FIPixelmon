package com.pixelmonmod.pixelmon.client.models;

import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimation;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class DualModelFactory extends PixelmonSmdFactory {
   protected ResourceLocation model2;
   protected float transparency;

   public DualModelFactory(EnumPokemonModel model, EnumPokemonModel model2) {
      super(model);
      this.model2 = model2.getModelLocation();
   }

   public DualModelFactory(ResourceLocation model, ResourceLocation model2) {
      super(model);
      this.model2 = model2;
   }

   public DualModelFactory setModel2Transparency(float transparency) {
      this.transparency = transparency;
      return this;
   }

   public PixelmonModelSmd createModel() {
      TransparentImpl impl = new TransparentImpl(this, loadModel(this.model), loadModel(this.model2));
      impl.body.func_78793_a(this.xRotation, this.yRotation, this.zRotation);
      impl.body.field_78795_f = this.rotateAngleX;
      impl.body.field_78796_g = this.rotateAngleY;
      impl.body2.func_78793_a(this.xRotation, this.yRotation, this.zRotation);
      impl.body2.field_78795_f = this.rotateAngleX;
      impl.body2.field_78796_g = this.rotateAngleY;
      impl.body2.setTransparent(this.transparency);
      impl.movementThreshold = this.movementThreshold;
      impl.animationIncrement = this.animationIncrement;
      return impl;
   }

   public static class TransparentImpl extends PixelmonSmdFactory.Impl {
      protected PixelmonModelRenderer body2;
      protected ValveStudioModel model2;

      TransparentImpl(PixelmonSmdFactory factory, ValveStudioModel model, ValveStudioModel model2) {
         super(factory, model);
         this.model2 = model2;
         this.body2 = new PixelmonModelRenderer(this, "body");
         this.body2.addCustomModel(new ModelCustomWrapper(model2));
      }

      public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
         super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
         this.body2.func_78785_a(f5);
      }

      protected void updateAnimation(IncrementingVariable variable) {
         SmdAnimation animation = this.theModel.currentSequence.checkForIncrement(variable);
         int frame = (int)Math.floor((double)(variable.value % (float)animation.totalFrames));
         animation.setCurrentFrame(frame);
         animation = this.model2.currentSequence.checkForIncrement(variable);
         frame = (int)Math.floor((double)(variable.value % (float)animation.totalFrames));
         animation.setCurrentFrame(frame);
         Minecraft.func_71410_x().field_71424_I.func_76320_a("pixelmon_animate");
         this.theModel.animate();
         this.model2.animate();
         Minecraft.func_71410_x().field_71424_I.func_76319_b();
      }

      protected void setAnimation(AnimationType animType) {
         this.theModel.setAnimation(animType);
         this.model2.setAnimation(animType);
      }
   }
}

package com.pixelmonmod.pixelmon.client.models.bikes;

import com.pixelmonmod.pixelmon.client.models.ModelCustomWrapper;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRenderer;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimation;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimationSequence;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.entities.bikes.EntityBike;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class ModelBike extends PixelmonModelSmd {
   PixelmonModelRenderer body = new PixelmonModelRenderer(this, "body");

   public ModelBike(EnumCustomModel model) {
      ValveStudioModel smd = (ValveStudioModel)model.getModel();
      this.body.addCustomModel(new ModelCustomWrapper(smd));
      this.body.func_78793_a(0.0F, 0.0F, 0.0F);
      this.animationIncrement = model == EnumCustomModel.AcroBike ? 1.75F : 1.25F;
      this.theModel = smd;
   }

   public void setupForRender(EntityBike bike) {
      if (bike.inc == null) {
         bike.inc = new IncrementingVariable(this.animationIncrement, 2.14748365E9F);
      }

      this.updateAnimation(bike.inc);
   }

   protected void updateAnimation(IncrementingVariable variable) {
      SmdAnimationSequence sequence = this.theModel.currentSequence;
      SmdAnimation animation = sequence.checkForIncrement(variable);
      variable.limit = (float)animation.totalFrames;
      int frame = (int)Math.floor((double)(variable.value % (float)animation.totalFrames));
      animation.setCurrentFrame(frame);
      Minecraft.func_71410_x().field_71424_I.func_76320_a("bike_animate");
      this.theModel.animate();
      Minecraft.func_71410_x().field_71424_I.func_76319_b();
   }

   public void render(Entity entity, float scale) {
      this.func_78088_a(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.body.func_78785_a(f5);
   }

   public String toString() {
      return this.theModel.resource.toString();
   }
}

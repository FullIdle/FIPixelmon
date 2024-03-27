package com.pixelmonmod.pixelmon.client.models.pokeballs;

import com.pixelmonmod.pixelmon.client.models.ModelCustomWrapper;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelRenderer;
import com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimation;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimationSequence;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import com.pixelmonmod.pixelmon.enums.EnumCustomModel;
import net.minecraft.entity.Entity;

public class ModelPokeballs extends PixelmonModelSmd {
   PixelmonModelRenderer body = new PixelmonModelRenderer(this, "body");

   public ModelPokeballs(EnumCustomModel model) {
      ValveStudioModel smd = (ValveStudioModel)model.getModel();
      this.body.addCustomModel(new ModelCustomWrapper(smd));
      this.body.func_78793_a(0.0F, 21.442F, 0.0F);
      this.theModel = smd;
   }

   public void doAnimation(Entity entity) {
      if (entity != null) {
         EntityPokeBall pokeball = (EntityPokeBall)entity;
         if (!pokeball.getAnimation().equals(pokeball.lastAnim)) {
            if (pokeball.inc != null) {
               pokeball.inc.value = 0.0F;
            }

            pokeball.lastAnim = pokeball.getAnimation();
         }

         if (pokeball.inc == null) {
            pokeball.inc = new IncrementingVariable(this.animationIncrement, 2.14748365E9F);
         } else {
            pokeball.inc.increment = this.animationIncrement;
         }

         if (this.theModel.hasAnimations() && this.theModel.currentSequence != null) {
            SmdAnimationSequence sequence = this.theModel.currentSequence;
            SmdAnimation animation = sequence.checkForIncrement(pokeball.inc);
            int frame;
            if (pokeball.inc.value < (float)animation.totalFrames) {
               frame = (int)(pokeball.inc.value % (float)animation.totalFrames);
            } else {
               frame = animation.totalFrames - 1;
            }

            animation.setCurrentFrame(frame);
            this.theModel.animate();
         }

      }
   }

   public void render(Entity entity, float scale) {
      this.func_78088_a(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.body.func_78785_a(f5);
   }
}
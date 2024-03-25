package com.pixelmonmod.pixelmon.client.models;

import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimation;
import com.pixelmonmod.pixelmon.client.models.smd.SmdAnimationSequence;
import com.pixelmonmod.pixelmon.client.models.smd.ValveStudioModel;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.IncrementingVariable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public abstract class PixelmonModelSmd extends PixelmonModelBase {
   public float animationIncrement = 1.0F;
   public ValveStudioModel theModel;
   /** @deprecated */
   @Deprecated
   public final float scale = 0.0F;
   public float partialTick;

   public PixelmonModelSmd() {
      this.registerAnimationCounters();
   }

   public void func_78088_a(Entity var1, float f, float f1, float f2, float f3, float f4, float f5) {
   }

   public float getScale() {
      return this.theModel.getScale();
   }

   public void setAnimationIncrement(float f) {
      this.animationIncrement = f;
   }

   public void setupForRender(Entity2Client pixelmon) {
      this.setAnimation(pixelmon.getCurrentAnimation());
      if (this.getCounter(-1, pixelmon) == null) {
         this.setCounter(-1, 2.14748365E9F, this.animationIncrement, pixelmon);
      }

      this.updateAnimation(this.getCounter(-1, pixelmon));
   }

   public void setupForRender(EntityStatue statue) {
      this.setAnimation(statue.getCurrentAnimation());
      if (statue.getAnimationVariables().getCounter(-1) == null) {
         statue.getAnimationVariables().setCounter(-1, 2.14748365E9F, this.animationIncrement);
      }

      this.updateAnimation(statue.getAnimationVariables().getCounter(-1));
   }

   protected void updateAnimation(IncrementingVariable variable) {
      SmdAnimationSequence sequence = this.theModel.currentSequence;
      SmdAnimation animation = sequence.checkForIncrement(variable);
      int frame = (int)Math.floor((double)(variable.value % (float)animation.totalFrames));
      animation.setCurrentFrame(frame);
      Minecraft.func_71410_x().field_71424_I.func_76320_a("pixelmon_animate");
      this.theModel.animate();
      Minecraft.func_71410_x().field_71424_I.func_76319_b();
   }

   protected void setAnimation(AnimationType animType) {
      this.theModel.setAnimation(animType);
   }

   public static boolean isMinecraftPaused() {
      Minecraft m = Minecraft.func_71410_x();
      return m.func_71356_B() && m.field_71462_r != null && m.field_71462_r.func_73868_f() && !m.func_71401_C().func_71344_c();
   }

   protected void setInt(int id, int value, Entity2Client pixelmon) {
      pixelmon.getAnimationVariables().setInt(id, value);
   }

   protected int getInt(int id, Entity2Client pixelmon) {
      return pixelmon.getAnimationVariables().getInt(id);
   }

   protected IncrementingVariable setCounter(int id, float limit, float increment, Entity2Client pixelmon) {
      pixelmon.getAnimationVariables().setCounter(id, limit, increment);
      return this.getCounter(id, pixelmon);
   }

   protected IncrementingVariable getCounter(int id, Entity2Client pixelmon) {
      return pixelmon.getAnimationVariables().getCounter(id);
   }

   protected void registerAnimationCounters() {
   }
}

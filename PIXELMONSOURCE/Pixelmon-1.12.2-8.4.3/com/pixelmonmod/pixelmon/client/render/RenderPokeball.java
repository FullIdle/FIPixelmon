package com.pixelmonmod.pixelmon.client.render;

import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.entities.pokeballs.EntityPokeBall;
import com.pixelmonmod.pixelmon.entities.pokeballs.EnumPokeBallMode;
import com.pixelmonmod.pixelmon.tools.Quarternion;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderPokeball extends Render {
   ModelPokeballs model;

   public RenderPokeball(RenderManager renderManager) {
      super(renderManager);
   }

   public void doRender(EntityPokeBall pokeball, double x, double y, double z, float f, float f1) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(x, y, z);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      Quarternion q1 = Quarternion.toQuarternion(-pokeball.getInitialPitch(), -((float)Math.cos((double)pokeball.getInitialYaw() * Math.PI / 180.0)), 0.0F, -((float)Math.sin((double)pokeball.getInitialYaw() * Math.PI / 180.0)));
      Quarternion q2 = Quarternion.toQuarternion(pokeball.field_70177_z, 0.0F, 1.0F, 0.0F);
      q1.multiplyBy(q2);
      q1.fromQuarternion(q1);
      GlStateManager.func_179114_b(q1.w, q1.x, q1.y, q1.z);
      this.model = pokeball.getModel();
      this.model.theModel.setAnimation(pokeball.getAnimation());
      String path = "pixelmon:textures/pokeballs/";
      this.func_110776_a(new ResourceLocation(path + pokeball.getType().getTexture()));
      if (!pokeball.getIsWaiting()) {
         pokeball.premierFlash();
      }

      if (pokeball.getMode() == EnumPokeBallMode.empty && pokeball.getAnimation() == AnimationType.BOUNCEOPEN) {
         pokeball.onCaptureAttemptEffect();
      }

      if (pokeball.shakeCount == (pokeball.getCritical() ? 1 : 4) && pokeball.getIsWaiting() && pokeball.waitTimer < 1) {
         GlStateManager.func_179131_c(0.006F, 0.006F, 0.006F, 1.0F);
         this.func_110776_a(new ResourceLocation(path + pokeball.getType().getFlashRedTexture()));
         pokeball.successfulCaptureEffect();
      }

      if (pokeball.getMode() == EnumPokeBallMode.full && pokeball.getAnimation() == AnimationType.BOUNCECLOSED) {
         pokeball.releaseEffect();
      }

      GlStateManager.func_179094_E();
      GlStateManager.func_179091_B();
      this.model.render(pokeball, EntityPokeBall.scale);
      GlStateManager.func_179101_C();
      GlStateManager.func_179121_F();
      GlStateManager.func_179121_F();
   }

   protected ResourceLocation getEntityTexture(EntityPokeBall entity) {
      return null;
   }
}

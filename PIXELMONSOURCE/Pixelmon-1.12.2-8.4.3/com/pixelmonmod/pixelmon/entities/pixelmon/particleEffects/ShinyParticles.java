package com.pixelmonmod.pixelmon.entities.pixelmon.particleEffects;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.AnimationVariables;
import net.minecraft.client.Minecraft;

public class ShinyParticles extends ParticleEffects {
   public void onUpdate(Entity2Client pixelmon) {
      if (!pixelmon.getAnimationVariables().hasInt(74)) {
         pixelmon.getAnimationVariables().setInt(74, RandomHelper.rand.nextInt(30) * -1);
      }

      int count = pixelmon.getAnimationVariables().getInt(74);
      AnimationVariables var10000 = pixelmon.getAnimationVariables();
      ++count;
      var10000.setInt(74, count);
      if (count == 1) {
         ParticleSystems.SHINY.getSystem().execute(Minecraft.func_71410_x(), pixelmon.field_70170_p, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, 1.0F, false, pixelmon.getBaseStats().getBoundsData().getWidth(), pixelmon.getBaseStats().getBoundsData().getHeight(), pixelmon.getBaseStats().getBoundsData().getWidth());
      } else if (count > 3) {
         pixelmon.getAnimationVariables().setInt(74, RandomHelper.rand.nextInt(10) * -1);
      }

   }
}

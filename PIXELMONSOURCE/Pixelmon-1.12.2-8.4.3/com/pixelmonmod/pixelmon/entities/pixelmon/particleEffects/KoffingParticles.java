package com.pixelmonmod.pixelmon.entities.pixelmon.particleEffects;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.animation.AnimationVariables;
import com.pixelmonmod.pixelmon.enums.EnumPixelmonParticles;
import net.minecraft.util.math.MathHelper;

public class KoffingParticles extends ParticleEffects {
   public void onUpdate(Entity2Client pixelmon) {
      float var2 = (float)(pixelmon.getPokemonData().getBaseStats().getBoundsData().getWidth() * (double)pixelmon.getPixelmonScale());
      float var4 = this.rand.nextFloat() * 3.1415927F * 2.0F;
      float var5 = this.rand.nextFloat() * 4.0F + 0.5F;
      float var6 = MathHelper.func_76126_a(var4) * var2 * 0.5F * var5;
      float var7 = MathHelper.func_76134_b(var4) * var2 * 0.5F * var5;
      if (!pixelmon.getAnimationVariables().hasInt(72)) {
         pixelmon.getAnimationVariables().setInt(72, RandomHelper.rand.nextInt(27) * -1);
      }

      int count = pixelmon.getAnimationVariables().getInt(72);
      AnimationVariables var10000 = pixelmon.getAnimationVariables();
      ++count;
      var10000.setInt(72, count);
      if (count > 0 && count < 3) {
         ClientProxy.spawnParticle(EnumPixelmonParticles.koffing, pixelmon.field_70170_p, pixelmon.field_70165_t + (double)var6, pixelmon.field_70163_u + 1.5, pixelmon.field_70161_v + (double)var7, pixelmon.getPokemonData().isShiny());
      } else if (count > 3) {
         pixelmon.getAnimationVariables().setInt(72, RandomHelper.rand.nextInt(27) * -1);
      }

   }
}

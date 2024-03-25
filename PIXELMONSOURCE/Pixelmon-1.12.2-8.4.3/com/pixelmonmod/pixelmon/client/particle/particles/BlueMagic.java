package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlueMagic extends ParticleEffect {
   private static final ResourceLocation tex = new ResourceLocation("pixelmon", "textures/particles/blue_magic.png");

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      particle.setRGBA(1.0F, 1.0F, 1.0F, 0.5F);
      particle.setMotion(particle.getMotionX() * 0.10000000149011612, particle.getMotionY() * 0.10000000149011612, particle.getMotionZ() * 0.10000000149011612);
      particle.setMotion(particle.getMotionX() + vx * 0.4, particle.getMotionY() + vy * 0.4, particle.getMotionZ() + vz * 0.4);
      particle.setScale(0.1F);
      particle.func_187114_a((int)(6.0 / (Math.random() * 0.8 + 0.6)));
      particle.func_187114_a((int)((float)particle.getMaxAge() * size));
   }

   public void update(ParticleArcanery particle) {
      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      particle.incrementAge();
      if (particle.getAge() >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

      particle.func_187110_a(particle.getMotionX(), particle.getMotionY(), particle.getMotionZ());
      particle.setMotion(particle.getMotionX() * 0.699999988079071, particle.getMotionY() * 0.699999988079071 - 0.019999999552965164, particle.getMotionZ() * 0.699999988079071);
      if (particle.onGround()) {
         particle.setMotion(particle.getMotionX() * 0.699999988079071, particle.getMotionY(), particle.getMotionZ() * 0.699999988079071);
      }

   }

   public ResourceLocation texture() {
      return tex;
   }
}

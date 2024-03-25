package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RedOrbShrinking extends ParticleEffect {
   private double tX;
   private double tY;
   private double tZ;
   private static final ResourceLocation tex = new ResourceLocation("pixelmon", "textures/particles/red_orb.png");

   public RedOrbShrinking(double tX, double tY, double tZ) {
      this.tX = tX;
      this.tY = tY;
      this.tZ = tZ;
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      particle.setRGBA(1.0F, 1.0F, 1.0F, 1.0F);
      particle.setHeading(-(x - this.tX), -(y - this.tY), -(z - this.tZ), 0.4F, 0.0F);
      particle.setScale(0.2F);
      particle.func_187114_a(20);
   }

   public void update(ParticleArcanery particle) {
      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      particle.incrementAge();
      if (particle.getAge() >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

      particle.func_187110_a(particle.getMotionX(), particle.getMotionY(), particle.getMotionZ());
      particle.setScale(0.2F * (1.0F - (float)particle.getAge() / (float)particle.getMaxAge()));
   }

   public ResourceLocation texture() {
      return tex;
   }
}

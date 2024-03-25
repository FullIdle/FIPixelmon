package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SlingRing extends ParticleEffect {
   private boolean parent;
   private int maxAge;
   private static final ResourceLocation tex = new ResourceLocation("pixelmon", "textures/particles/sling_ring.png");

   public SlingRing(boolean parent, int maxAge) {
      this.parent = parent;
      this.maxAge = maxAge;
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      particle.setRGBA(1.0F, 1.0F, 1.0F, this.parent ? 0.0F : 0.5F);
      particle.setScale(0.75F);
      particle.func_187114_a(this.maxAge);
   }

   public void update(ParticleArcanery particle) {
      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      particle.incrementAge();
      if (particle.getAge() >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

      if (this.parent) {
         ParticleArcanery child = new ParticleArcanery(particle.getWorld(), particle.getX(), particle.getY(), particle.getZ(), 0.0, 0.0, 0.0, new SlingRing(false, 5));
         Minecraft.func_71410_x().field_71452_i.func_78873_a(child);
      } else {
         particle.setMotion(0.2 * (particle.getRand().nextDouble() * 2.0 - 1.0), 0.2 * (particle.getRand().nextDouble() * 2.0 - 1.0), 0.2 * (particle.getRand().nextDouble() * 2.0 - 1.0));
         particle.func_187110_a(particle.getMotionX(), particle.getMotionY(), particle.getMotionZ());
         particle.setRGBA(1.0F, 1.0F, 1.0F, 1.0F - (float)particle.getAge() / (float)particle.getMaxAge());
         particle.setScale(0.75F - (float)particle.getAge() / (float)particle.getMaxAge() * 0.75F);
      }

   }

   public ResourceLocation texture() {
      return tex;
   }
}

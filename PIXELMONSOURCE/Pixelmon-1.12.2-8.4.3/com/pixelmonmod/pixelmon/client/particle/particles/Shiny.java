package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Shiny extends ParticleEffect {
   private final double w;
   private final double h;
   private final double l;
   private static final ResourceLocation tex = new ResourceLocation("pixelmon", "textures/particles/shiny.png");

   public Shiny(double w, double h, double l) {
      this.w = w * 2.0;
      this.h = h + 0.8;
      this.l = l * 2.0;
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      particle.setRGBA(1.0F, 0.8F, 0.3F, 0.0F);
      particle.setScale((float)(Math.random() * 0.20000000298023224) + 0.05F);
      particle.func_187114_a(14);
      particle.setPos(x + world.field_73012_v.nextDouble() * this.w - this.w / 2.0, y + 0.2 + world.field_73012_v.nextDouble() * this.h, z + world.field_73012_v.nextDouble() * this.l - this.l / 2.0);
   }

   public void update(ParticleArcanery particle) {
      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      particle.incrementAge();
      if (particle.getAge() >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

      int x = particle.getAge();
      int m = particle.getMaxAge();
      int h = m / 2;
      float opacity = (float)(x <= h ? x : h - (x - h)) / (float)h;
      particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), opacity);
   }

   public ResourceLocation texture() {
      return tex;
   }
}

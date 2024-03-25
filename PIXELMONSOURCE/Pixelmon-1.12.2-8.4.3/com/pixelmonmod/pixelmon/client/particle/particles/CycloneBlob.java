package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CycloneBlob extends ParticleEffect {
   private double random;
   private double mx;
   private double mz;
   private double theta;
   private double wu;
   private double r;
   private boolean yellow;
   private static final ResourceLocation tex = new ResourceLocation("pixelmon", "textures/particles/orb.png");

   public CycloneBlob(double random, double theta, double wu, double r, double y) {
      this.random = random;
      this.theta = theta;
      this.wu = wu;
      this.r = r;
      this.yellow = y == 1.0;
   }

   public void preRender(ParticleArcanery particle, float partialTicks) {
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      Random rand = new Random();
      particle.setRGBA(1.0F, this.yellow ? 1.0F : 0.0F, this.yellow ? 1.0F : 0.0F, 0.4F);
      particle.setMotion(particle.getMotionX() * 0.10000000149011612 + this.random * (rand.nextGaussian() - 0.5), particle.getMotionY() * 0.10000000149011612, particle.getMotionZ() * 0.10000000149011612 + this.random * (rand.nextGaussian() - 0.5));
      particle.setMotion(particle.getMotionX() + vx, particle.getMotionY() + vy, particle.getMotionZ() + vz);
      this.mx = vx;
      this.mz = vz;
      particle.setScale(0.55F);
      particle.func_187114_a(10);
   }

   public void update(ParticleArcanery particle) {
      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      particle.incrementAge();
      if (particle.getAge() >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

      particle.setScale(particle.getScale() / 45.0F * (float)(45 - particle.getAge()));
      particle.func_187110_a(particle.getMotionX(), particle.getMotionY(), particle.getMotionZ());
      this.theta += this.wu * (double)particle.getAge() / 2.0;
      this.mx = this.r * Math.cos(this.theta) / 6.0;
      this.mz = this.r * Math.sin(this.theta) / 6.0;
      particle.setMotion(this.mx, particle.getMotionY(), this.mz);
      particle.setRGBA(Math.min(0.1F + 0.8F / ((float)particle.getAge() / 2.5F), 1.0F), this.yellow ? Math.min(0.1F + 0.8F / ((float)particle.getAge() / 2.5F), 1.0F) : 0.0F, this.yellow ? Math.min(0.1F + 0.8F / ((float)particle.getAge() / 2.5F), 1.0F) : 0.0F, 0.4F);
   }

   public ResourceLocation texture() {
      return tex;
   }
}

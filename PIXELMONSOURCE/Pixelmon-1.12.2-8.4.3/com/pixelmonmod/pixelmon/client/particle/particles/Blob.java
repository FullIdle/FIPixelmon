package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Blob extends ParticleEffect {
   private double random;
   private double mx;
   private double mz;
   private double theta;
   private double wu;
   private double r;
   private float red;
   private float green;
   private float blue;
   private boolean white;
   private int maxAge;
   private int colorPattern;
   private float scale;
   private static final ResourceLocation tex = new ResourceLocation("pixelmon", "textures/particles/ultra.png");

   public Blob(double random, double theta, double wu, double r, float red, float green, float blue, boolean white, int maxAge, int colorPattern, float scale) {
      this.random = random;
      this.theta = theta;
      this.wu = wu;
      this.r = r;
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.white = white;
      this.maxAge = maxAge;
      this.colorPattern = colorPattern;
      this.scale = scale;
   }

   public void preRender(ParticleArcanery particle, float partialTicks) {
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.func_179131_c(this.red, this.green, this.blue, particle.getAlphaF());
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      particle.setRGBA(this.white ? 0.9F : this.red, this.white ? 0.9F : this.green, this.white ? 1.0F : this.blue, this.white ? 0.1F : 0.4F);
      particle.setMotion(vx, vy, vz);
      this.mx = vx;
      this.mz = vz;
      particle.setScale(this.scale);
      particle.func_187114_a(this.maxAge);
   }

   public void update(ParticleArcanery particle) {
      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      particle.incrementAge();
      if (particle.getAge() >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

      float newScale = this.scale * (1.0F - (float)particle.getAge() / (float)particle.getMaxAge());
      particle.setScale(newScale);
      this.theta += this.wu * (double)particle.getAge() / 2.0;
      this.mx = this.r * Math.cos(this.theta) / 18.0;
      this.mz = this.r * Math.sin(this.theta) / 18.0;
      particle.setMotion(this.mx, this.mz, particle.getMotionZ() - 0.03);
      if (particle.getMotionZ() < -0.75) {
         particle.setMotion(this.mx, this.mz, -0.75);
      }

      if (this.white) {
         particle.setRGBA(0.9F, 0.9F, 1.0F, 0.1F);
      } else {
         int colorSpeed = 40;
         int colorDeterminant = (particle.getAge() + (int)particle.getWorld().func_82737_E() % colorSpeed) % colorSpeed;
         particle.setRGBA(this.calcR(colorSpeed, colorDeterminant), this.calcG(colorSpeed, colorDeterminant), this.calcB(colorSpeed, colorDeterminant), 0.4F);
      }

   }

   public float calcChangingColor(int colorSpeed, int colorDeterminant) {
      float c1 = 1.0F / (float)colorSpeed;
      float c2 = c1 * (float)colorDeterminant;
      if (c2 > 0.5F) {
         float c3 = c2 - 0.5F;
         c2 -= c3 * 2.0F;
      }

      return c2;
   }

   public float calcR(int colorSpeed, int colorDeterminant) {
      switch (this.colorPattern) {
         case 0:
         case 1:
            return this.calcChangingColor(colorSpeed, colorDeterminant);
         case 2:
         case 3:
            return 0.2F;
         case 4:
         case 5:
            return 0.7F;
         default:
            return 0.0F;
      }
   }

   public float calcG(int colorSpeed, int colorDeterminant) {
      switch (this.colorPattern) {
         case 0:
         case 5:
            return 0.2F;
         case 1:
         case 3:
            return 0.7F;
         case 2:
         case 4:
            return this.calcChangingColor(colorSpeed, colorDeterminant);
         default:
            return 0.0F;
      }
   }

   public float calcB(int colorSpeed, int colorDeterminant) {
      switch (this.colorPattern) {
         case 0:
         case 2:
            return 0.7F;
         case 1:
         case 4:
            return 0.2F;
         case 3:
         case 5:
            return this.calcChangingColor(colorSpeed, colorDeterminant);
         default:
            return 0.0F;
      }
   }

   public ResourceLocation texture() {
      return tex;
   }
}

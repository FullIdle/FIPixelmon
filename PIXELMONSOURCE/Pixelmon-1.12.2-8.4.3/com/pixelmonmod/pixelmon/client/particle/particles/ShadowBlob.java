package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ShadowBlob extends ParticleEffect {
   private double random;
   private static final ResourceLocation tex1 = new ResourceLocation("pixelmon", "textures/particles/shadow1.png");
   private static final ResourceLocation tex2 = new ResourceLocation("pixelmon", "textures/particles/shadow2.png");
   private static final ResourceLocation tex3 = new ResourceLocation("pixelmon", "textures/particles/shadow3.png");

   public ShadowBlob(double random) {
      this.random = random;
   }

   public void preRender(ParticleArcanery particle, float partialTicks) {
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      Random rand = new Random();
      particle.setRGBA(1.0F - rand.nextFloat() / 10.0F, 1.0F - rand.nextFloat() / 10.0F, 1.0F - rand.nextFloat() / 10.0F, 1.0F - rand.nextFloat() / 10.0F);
      particle.setMotion(particle.getMotionX() * 0.10000000149011612 + this.random * (rand.nextGaussian() - 0.5), particle.getMotionY() * 0.10000000149011612 + this.random * (rand.nextGaussian() - 0.5), particle.getMotionZ() * 0.10000000149011612 + this.random * (rand.nextGaussian() - 0.5));
      particle.setScale(0.15F);
      particle.func_187114_a(6);
   }

   public void update(ParticleArcanery particle) {
      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      particle.incrementAge();
      if (particle.getAge() >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

      particle.func_82338_g((float)particle.getAge() / (float)particle.getMaxAge());
      particle.func_187110_a(particle.getMotionX(), particle.getMotionY(), particle.getMotionZ());
      particle.setMotion(particle.getMotionX() * 0.699999988079071, particle.getMotionY() * 0.699999988079071, particle.getMotionZ() * 0.699999988079071);
   }

   public ResourceLocation texture() {
      switch ((new Random()).nextInt(3)) {
         case 0:
            return tex1;
         case 1:
            return tex2;
         default:
            return tex3;
      }
   }
}

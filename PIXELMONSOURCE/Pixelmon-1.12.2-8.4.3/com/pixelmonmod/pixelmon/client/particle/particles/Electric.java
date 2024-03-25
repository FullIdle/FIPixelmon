package com.pixelmonmod.pixelmon.client.particle.particles;

import com.google.common.primitives.Doubles;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class Electric extends ParticleEffect {
   private int age;
   private boolean parent;
   private float pitch;
   private float yaw;
   private float velocity;
   private float innaccuracy;
   private float r;
   private float g;
   private float b;
   private float a;
   private static final ResourceLocation tex = new ResourceLocation("pixelmon", "textures/particles/lightning4.png");

   public Electric(int age, boolean parent, float pitch, float yaw, float velocity, float innaccuracy, float r, float g, float b) {
      this.age = age;
      this.parent = parent;
      this.pitch = pitch;
      this.yaw = yaw;
      this.velocity = velocity;
      this.innaccuracy = innaccuracy;
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = 1.0F;
   }

   public Electric(int age, boolean parent, float pitch, float yaw, float velocity, float innaccuracy, float r, float g, float b, float a) {
      this.age = age;
      this.parent = parent;
      this.pitch = pitch;
      this.yaw = yaw;
      this.velocity = velocity;
      this.innaccuracy = innaccuracy;
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = a;
   }

   public void render(ParticleArcanery particle, Tessellator tessellator, float partialTicks) {
      if (!this.parent) {
         Minecraft mc = Minecraft.func_71410_x();
         GlStateManager.func_179094_E();
         GlStateManager.func_179090_x();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 1);
         Entity viewpoint = mc.func_175606_aa() == null ? mc.field_71439_g : mc.func_175606_aa();
         float tx = (float)(((Entity)viewpoint).field_70169_q + (((Entity)viewpoint).field_70165_t - ((Entity)viewpoint).field_70169_q) * (double)mc.func_184121_ak());
         float ty = (float)(((Entity)viewpoint).field_70167_r + (((Entity)viewpoint).field_70163_u - ((Entity)viewpoint).field_70167_r) * (double)mc.func_184121_ak());
         float tz = (float)(((Entity)viewpoint).field_70166_s + (((Entity)viewpoint).field_70161_v - ((Entity)viewpoint).field_70166_s) * (double)mc.func_184121_ak());
         GlStateManager.func_179109_b(-tx, -ty, -tz);
         GlStateManager.func_179131_c(this.r, this.g, this.b, 0.75F * this.a * (1.0F - (float)particle.getAge() / (float)particle.getMaxAge()));
         GL11.glEnable(2848);
         GlStateManager.func_187441_d(3.0F);
         GL11.glHint(3154, 4354);
         GL11.glBegin(3);
         GL11.glVertex3d(particle.getPrevX(), particle.getPrevY(), particle.getPrevZ());
         GL11.glVertex3d(particle.getX(), particle.getY(), particle.getZ());
         GL11.glEnd();
         GlStateManager.func_187441_d(8.0F);
         GlStateManager.func_179131_c(this.r, this.g, this.b, Math.max(0.75F * this.a * (1.0F - (float)particle.getAge() / (float)particle.getMaxAge()) - 0.3F, 0.0F));
         GL11.glBegin(3);
         GL11.glVertex3d(particle.getPrevX(), particle.getPrevY(), particle.getPrevZ());
         GL11.glVertex3d(particle.getX(), particle.getY(), particle.getZ());
         GL11.glEnd();
         GL11.glDisable(2848);
         GlStateManager.func_187441_d(2.0F);
         GlStateManager.func_179084_k();
         GlStateManager.func_179098_w();
         GlStateManager.func_179121_F();
      }

   }

   public boolean customRenderer() {
      return true;
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      particle.setRGBA(1.0F, 1.0F, 1.0F, 0.8F * this.a);
      particle.setScale(0.05F);
      particle.func_187114_a(this.age);
      particle.setPos(x, y, z);
      particle.setPrevPos(vx, vy, vz);
      this.setAim(this.pitch, this.yaw, this.velocity, this.innaccuracy, particle);
   }

   public void update(ParticleArcanery particle) {
      if (this.parent) {
         particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
         particle.func_187110_a(particle.getMotionX(), particle.getMotionY(), particle.getMotionZ());
         particle.func_187110_a((particle.getRand().nextDouble() - 0.5) * Doubles.constrainToRange(0.15 + (double)particle.getAge() / 3.0, 0.0, 1.75), (particle.getRand().nextDouble() - 0.5) * Doubles.constrainToRange(0.15 + (double)particle.getAge() / 3.0, 0.0, 1.75), (particle.getRand().nextDouble() - 0.5) * Doubles.constrainToRange(0.15 + (double)particle.getAge() / 3.0, 0.0, 1.75));
         ParticleArcanery child = new ParticleArcanery(particle.getWorld(), particle.getX(), particle.getY(), particle.getZ(), particle.getPrevX(), particle.getPrevY(), particle.getPrevZ(), new Electric(8, false, 0.0F, 0.0F, 0.0F, 0.0F, this.r, this.g, this.b, this.a));
         Minecraft.func_71410_x().field_71452_i.func_78873_a(child);
      }

      particle.incrementAge();
      if (particle.getAge() >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

   }

   public ResourceLocation texture() {
      return tex;
   }

   private void setAim(float pitch, float yaw, float velocity, float inaccuracy, ParticleArcanery particle) {
      float f = -MathHelper.func_76126_a(yaw * 0.017453292F) * MathHelper.func_76134_b(pitch * 0.017453292F);
      float f1 = -MathHelper.func_76126_a(pitch * 0.017453292F);
      float f2 = MathHelper.func_76134_b(yaw * 0.017453292F) * MathHelper.func_76134_b(pitch * 0.017453292F);
      this.setHeading((double)f, (double)f1, (double)f2, velocity, inaccuracy, particle);
   }

   private void setHeading(double x, double y, double z, float velocity, float inaccuracy, ParticleArcanery particle) {
      float f = MathHelper.func_76133_a(x * x + y * y + z * z);
      x /= (double)f;
      y /= (double)f;
      z /= (double)f;
      x += particle.getRand().nextGaussian() * 0.007499999832361937 * (double)inaccuracy;
      y += particle.getRand().nextGaussian() * 0.007499999832361937 * (double)inaccuracy;
      z += particle.getRand().nextGaussian() * 0.007499999832361937 * (double)inaccuracy;
      x *= (double)velocity;
      y *= (double)velocity;
      z *= (double)velocity;
      particle.setMotion(x, y, z);
   }
}

package com.pixelmonmod.pixelmon.client.particle.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleEffect;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Leaf extends ParticleEffect {
   private double swingArc;
   private short timeOnGround = 0;
   private double currentRot = 0.0;
   private boolean rotIncreasing = true;
   private float scale;
   private boolean shiny;
   private static final ResourceLocation tex1 = new ResourceLocation("pixelmon", "textures/particles/petal.png");
   private static final ResourceLocation tex2 = new ResourceLocation("pixelmon", "textures/particles/leaf.png");

   public Leaf(double swingArc, float scale, boolean shiny) {
      this.swingArc = swingArc;
      this.scale = scale;
      this.shiny = shiny;
   }

   public void preRender(ParticleArcanery particle, float partialTicks) {
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
   }

   public boolean customRenderer() {
      return true;
   }

   public void render(ParticleArcanery particle, Tessellator tessellator, float partialTicks) {
      Minecraft mc = Minecraft.func_71410_x();
      mc.func_110434_K().func_110577_a(particle.getTexture());
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181711_k);
      float f4 = particle.getScale();
      int combined = 15728880;
      int k3 = combined >> 16 & '\uffff';
      int l3 = combined & '\uffff';
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      float f5 = (float)(particle.getPrevX() + (particle.getX() - particle.getPrevX()) * (double)partialTicks - ParticleArcanery.field_70556_an);
      float f6 = (float)(particle.getPrevY() + (particle.getY() - particle.getPrevY()) * (double)partialTicks - ParticleArcanery.field_70554_ao);
      float f7 = (float)(particle.getPrevZ() + (particle.getZ() - particle.getPrevZ()) * (double)partialTicks - ParticleArcanery.field_70555_ap);
      int i = particle.func_189214_a(partialTicks);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      Vec3d[] avec3d = new Vec3d[]{new Vec3d((double)(-particle.rotationX * f4 - particle.rotationXY * f4), (double)(-particle.rotationZ * f4), (double)(-particle.rotationYZ * f4 - particle.rotationXZ * f4)), new Vec3d((double)(-particle.rotationX * f4 + particle.rotationXY * f4), (double)(particle.rotationZ * f4), (double)(-particle.rotationYZ * f4 + particle.rotationXZ * f4)), new Vec3d((double)(particle.rotationX * f4 + particle.rotationXY * f4), (double)(particle.rotationZ * f4), (double)(particle.rotationYZ * f4 + particle.rotationXZ * f4)), new Vec3d((double)(particle.rotationX * f4 - particle.rotationXY * f4), (double)(-particle.rotationZ * f4), (double)(particle.rotationYZ * f4 - particle.rotationXZ * f4))};
      if (particle.getAngle() != 0.0F) {
         float f8 = particle.getAngle() + (particle.getAngle() - particle.getPrevAngle()) * partialTicks;
         float f9 = MathHelper.func_76134_b(f8 * 0.5F);
         float f10 = MathHelper.func_76126_a(f8 * 0.5F) * (float)ParticleArcanery.field_190016_K.field_72450_a;
         float f11 = MathHelper.func_76126_a(f8 * 0.5F) * (float)ParticleArcanery.field_190016_K.field_72448_b;
         float f12 = MathHelper.func_76126_a(f8 * 0.5F) * (float)ParticleArcanery.field_190016_K.field_72449_c;
         Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

         for(int l = 0; l < 4; ++l) {
            avec3d[l] = vec3d.func_186678_a(2.0 * avec3d[l].func_72430_b(vec3d)).func_178787_e(avec3d[l].func_186678_a((double)(f9 * f9) - vec3d.func_72430_b(vec3d))).func_178787_e(vec3d.func_72431_c(avec3d[l]).func_186678_a((double)(2.0F * f9)));
         }
      }

      tessellator.func_178180_c().func_181662_b((double)f5 + avec3d[0].field_72450_a, (double)f6 + avec3d[0].field_72448_b, (double)f7 + avec3d[0].field_72449_c).func_187315_a(0.0, 1.0).func_187314_a(k3, l3).func_181666_a(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF()).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)f5 + avec3d[1].field_72450_a, (double)f6 + avec3d[1].field_72448_b, (double)f7 + avec3d[1].field_72449_c).func_187315_a(1.0, 1.0).func_187314_a(k3, l3).func_181666_a(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF()).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)f5 + avec3d[2].field_72450_a, (double)f6 + avec3d[2].field_72448_b, (double)f7 + avec3d[2].field_72449_c).func_187315_a(1.0, 0.0).func_187314_a(k3, l3).func_181666_a(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF()).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)f5 + avec3d[3].field_72450_a, (double)f6 + avec3d[3].field_72448_b, (double)f7 + avec3d[3].field_72449_c).func_187315_a(0.0, 0.0).func_187314_a(k3, l3).func_181666_a(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF()).func_181675_d();
      tessellator.func_78381_a();
   }

   public void init(ParticleArcanery particle, World world, double x, double y, double z, double vx, double vy, double vz, float size) {
      new Random();
      particle.setRGBA(1.0F, 1.0F, 1.0F, 1.0F);
      particle.setMotion(particle.getMotionX() + vx, particle.getMotionY() + vy, particle.getMotionZ() + vz);
      particle.setScale(this.scale / 3.0F);
      particle.func_187114_a(40);
   }

   public void update(ParticleArcanery particle) {
      particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
      particle.incrementAge();
      if (particle.getAge() > 15 && Minecraft.func_71410_x().field_71441_e.func_180495_p(new BlockPos(particle.getX(), particle.getY() - 0.3, particle.getZ())).func_185896_q()) {
         particle.setOnGround();
      }

      if (particle.onGround()) {
         ++this.timeOnGround;
         particle.setRGBA(1.0F, 1.0F - (float)this.timeOnGround / (float)particle.getMaxAge() / 2.5F, 1.0F - (float)this.timeOnGround / (float)particle.getMaxAge(), 1.0F - (float)this.timeOnGround / (float)particle.getMaxAge());
      } else {
         if (particle.getMotionY() < 0.0) {
            if (this.rotIncreasing) {
               this.currentRot += 0.2 * Math.max(0.2, Math.abs(this.swingArc + this.currentRot));
               particle.setAngle((float)this.currentRot);
               if ((double)particle.getAngle() >= this.swingArc) {
                  this.rotIncreasing = false;
               }
            } else {
               this.currentRot -= 0.2 * Math.max(0.2, Math.abs(this.swingArc - this.currentRot));
               particle.setAngle((float)this.currentRot);
               if ((double)particle.getAngle() <= -this.swingArc) {
                  this.rotIncreasing = true;
               }
            }
         }

         particle.func_187110_a(particle.getMotionX(), particle.getMotionY(), particle.getMotionZ());
         particle.setMotion(particle.getMotionX() / 2.0, Math.max(-0.15, particle.getMotionY() - 0.1), particle.getMotionZ() / 2.0);
      }

      if (this.timeOnGround >= particle.getMaxAge()) {
         particle.func_187112_i();
      }

   }

   public ResourceLocation texture() {
      return this.shiny ? tex1 : tex2;
   }
}

package com.pixelmonmod.pixelmon.client.particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleArcanery extends Particle {
   private static final List queue = new ArrayList();
   public ParticleEffect effect;
   private float f;
   public float rotationX;
   public float rotationZ;
   public float rotationYZ;
   public float rotationXY;
   public float rotationXZ;

   public ParticleArcanery(World world, double x, double y, double z, double vx, double vy, double vz, ParticleEffect effect) {
      this(world, x, y, z, vx, vy, vz, 0.5F, effect);
   }

   public ParticleArcanery(World world, double x, double y, double z, double vx, double vy, double vz, float size, ParticleEffect effect) {
      super(world, x, y, z, vx, vy, vz);
      this.effect = effect;
      effect.init(this, world, x, y, z, vx, vy, vz, size);
      this.func_189213_a();
   }

   public void setRGBA(float r, float g, float b, float a) {
      this.field_70552_h = r;
      this.field_70553_i = g;
      this.field_70551_j = b;
      this.field_82339_as = a;
   }

   public double getMotionX() {
      return this.field_187129_i;
   }

   public double getMotionY() {
      return this.field_187130_j;
   }

   public double getMotionZ() {
      return this.field_187131_k;
   }

   public void setMotion(double x, double y, double z) {
      this.field_187129_i = x;
      this.field_187130_j = y;
      this.field_187131_k = z;
   }

   public void setOnGround() {
      this.field_187132_l = true;
   }

   public void func_187114_a(int a) {
      this.field_70547_e = a;
   }

   public int getMaxAge() {
      return this.field_70547_e;
   }

   public void incrementAge() {
      ++this.field_70546_d;
   }

   public int getAge() {
      return this.field_70546_d;
   }

   public void setScale(float s) {
      this.field_70544_f = s;
   }

   public float getScale() {
      return this.field_70544_f;
   }

   public double getX() {
      return this.field_187126_f;
   }

   public double getY() {
      return this.field_187127_g;
   }

   public double getZ() {
      return this.field_187128_h;
   }

   public void setPos(double x, double y, double z) {
      this.field_187126_f = x;
      this.field_187127_g = y;
      this.field_187128_h = z;
   }

   public double getPrevX() {
      return this.field_187123_c;
   }

   public double getPrevY() {
      return this.field_187124_d;
   }

   public double getPrevZ() {
      return this.field_187125_e;
   }

   public void setPrevPos(double x, double y, double z) {
      this.field_187123_c = x;
      this.field_187124_d = y;
      this.field_187125_e = z;
   }

   public void setHeading(double x, double y, double z, float velocity, float inaccuracy) {
      float f = MathHelper.func_76133_a(x * x + y * y + z * z);
      x /= (double)f;
      y /= (double)f;
      z /= (double)f;
      x += this.field_187136_p.nextGaussian() * 0.007499999832361937 * (double)inaccuracy;
      y += this.field_187136_p.nextGaussian() * 0.007499999832361937 * (double)inaccuracy;
      z += this.field_187136_p.nextGaussian() * 0.007499999832361937 * (double)inaccuracy;
      x *= (double)velocity;
      y *= (double)velocity;
      z *= (double)velocity;
      this.field_187129_i = x;
      this.field_187130_j = y;
      this.field_187131_k = z;
   }

   public boolean onGround() {
      return this.field_187132_l;
   }

   public static void dispatch(Tessellator tessellator, float ticks) {
      ParticleEvents.arcaneryParticleCount = 0;
      Iterator var2 = queue.iterator();

      while(var2.hasNext()) {
         ParticleArcanery particle = (ParticleArcanery)var2.next();
         if (particle != null) {
            if (particle.getEffect().customRenderer()) {
               particle.getEffect().preRender(particle, ticks);
               particle.getEffect().render(particle, tessellator, ticks);
               particle.getEffect().postRender(particle, ticks);
               GlStateManager.func_179117_G();
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
               Minecraft mc = Minecraft.func_71410_x();
               ResourceLocation texture = particle.getTexture();
               if (texture != null) {
                  mc.func_110434_K().func_110577_a(particle.getTexture());
               }

               GlStateManager.func_179117_G();
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               particle.getEffect().preRender(particle, ticks);
               GlStateManager.func_179141_d();
               GlStateManager.func_179147_l();
               GlStateManager.func_179112_b(770, 771);
               tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181711_k);
               particle.render(tessellator, ticks);
               tessellator.func_78381_a();
               particle.getEffect().postRender(particle, ticks);
            }
         }
      }

      queue.clear();
   }

   public void func_180434_a(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
      this.f = partialTicks;
      this.rotationX = rotationX;
      this.rotationZ = rotationZ;
      this.rotationYZ = rotationYZ;
      this.rotationXY = rotationXY;
      this.rotationXZ = rotationXZ;
      queue.add(this);
   }

   public float getAngle() {
      return this.field_190014_F;
   }

   public float getPrevAngle() {
      return this.field_190015_G;
   }

   public float getAlphaF() {
      return this.field_82339_as;
   }

   public ResourceLocation getTexture() {
      return this.effect.texture();
   }

   public void render(Tessellator tessellator, float ticks) {
      ++ParticleEvents.arcaneryParticleCount;
      float f4 = this.field_70544_f;
      int combined = 15728880;
      int k3 = combined >> 16 & '\uffff';
      int l3 = combined & '\uffff';
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      float f5 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)ticks - field_70556_an);
      float f6 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)ticks - field_70554_ao);
      float f7 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)ticks - field_70555_ap);
      int i = this.func_189214_a(ticks);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      Vec3d[] avec3d = new Vec3d[]{new Vec3d((double)(-this.rotationX * f4 - this.rotationXY * f4), (double)(-this.rotationZ * f4), (double)(-this.rotationYZ * f4 - this.rotationXZ * f4)), new Vec3d((double)(-this.rotationX * f4 + this.rotationXY * f4), (double)(this.rotationZ * f4), (double)(-this.rotationYZ * f4 + this.rotationXZ * f4)), new Vec3d((double)(this.rotationX * f4 + this.rotationXY * f4), (double)(this.rotationZ * f4), (double)(this.rotationYZ * f4 + this.rotationXZ * f4)), new Vec3d((double)(this.rotationX * f4 - this.rotationXY * f4), (double)(-this.rotationZ * f4), (double)(this.rotationYZ * f4 - this.rotationXZ * f4))};
      if (this.field_190014_F != 0.0F) {
         float f8 = this.field_190014_F + (this.field_190014_F - this.field_190015_G) * ticks;
         float f9 = MathHelper.func_76134_b(f8 * 0.5F);
         float f10 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72450_a;
         float f11 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72448_b;
         float f12 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72449_c;
         Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

         for(int l = 0; l < 4; ++l) {
            avec3d[l] = vec3d.func_186678_a(2.0 * avec3d[l].func_72430_b(vec3d)).func_178787_e(avec3d[l].func_186678_a((double)(f9 * f9) - vec3d.func_72430_b(vec3d))).func_178787_e(vec3d.func_72431_c(avec3d[l]).func_186678_a((double)(2.0F * f9)));
         }
      }

      tessellator.func_178180_c().func_181662_b((double)f5 + avec3d[0].field_72450_a, (double)f6 + avec3d[0].field_72448_b, (double)f7 + avec3d[0].field_72449_c).func_187315_a(0.0, 1.0).func_187314_a(k3, l3).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)f5 + avec3d[1].field_72450_a, (double)f6 + avec3d[1].field_72448_b, (double)f7 + avec3d[1].field_72449_c).func_187315_a(1.0, 1.0).func_187314_a(k3, l3).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)f5 + avec3d[2].field_72450_a, (double)f6 + avec3d[2].field_72448_b, (double)f7 + avec3d[2].field_72449_c).func_187315_a(1.0, 0.0).func_187314_a(k3, l3).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)f5 + avec3d[3].field_72450_a, (double)f6 + avec3d[3].field_72448_b, (double)f7 + avec3d[3].field_72449_c).func_187315_a(0.0, 0.0).func_187314_a(k3, l3).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_181675_d();
   }

   public boolean func_187111_c() {
      return true;
   }

   public int func_70537_b() {
      return 3;
   }

   public ParticleEffect getEffect() {
      return this.effect;
   }

   public void func_189213_a() {
      this.effect.update(this);
   }

   public void setAngle(float theta) {
      this.field_190015_G = this.field_190014_F;
      this.field_190014_F = theta;
   }

   public Random getRand() {
      return this.field_187136_p;
   }

   public World getWorld() {
      return this.field_187122_b;
   }
}

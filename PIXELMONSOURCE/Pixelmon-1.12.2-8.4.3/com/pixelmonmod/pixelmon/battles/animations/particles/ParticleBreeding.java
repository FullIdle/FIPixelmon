package com.pixelmonmod.pixelmon.battles.animations.particles;

import com.pixelmonmod.pixelmon.enums.EnumBreedingParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleHeart;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleBreeding extends ParticleHeart {
   private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
   public EnumBreedingParticles particleHeart;

   public ParticleBreeding(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, EnumBreedingParticles enumParticle) {
      this(par1World, par2, par4, par6, par8, par10, par12, 2.0F, enumParticle);
   }

   public ParticleBreeding(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float par14, EnumBreedingParticles enumParticle) {
      super(par1World, par2, par4, par6, 0.0, 0.0, 0.0);
      this.particleHeart = EnumBreedingParticles.grey;
      this.particleHeart = enumParticle;
      this.field_187129_i *= 0.009999999776482582;
      this.field_187130_j *= 0.009999999776482582;
      this.field_187131_k *= 0.009999999776482582;
      this.field_187130_j += 0.1;
      this.field_70544_f *= 0.45F;
      this.field_70544_f *= par14;
      this.field_70547_e = 16;
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

      this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
      if (this.field_187127_g == this.field_187124_d) {
         this.field_187129_i *= 1.1;
         this.field_187131_k *= 1.1;
      }

      this.field_187129_i *= 0.8600000143051147;
      this.field_187130_j *= 0.8600000143051147;
      this.field_187131_k *= 0.8600000143051147;
      if (this.field_187132_l) {
         this.field_187129_i *= 0.699999988079071;
         this.field_187131_k *= 0.699999988079071;
      }

   }

   public void func_180434_a(BufferBuilder buffer, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
      float f10 = 0.1F * this.field_70544_f;
      float f11 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)p_180434_3_ - field_70556_an);
      float f12 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)p_180434_3_ - field_70554_ao);
      float f13 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)p_180434_3_ - field_70555_ap);
      Tessellator.func_178181_a().func_78381_a();
      Minecraft.func_71410_x().field_71446_o.func_110577_a(this.particleHeart.location);
      buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      buffer.func_181662_b((double)(f11 - p_180434_4_ * f10 - p_180434_7_ * f10), (double)(f12 - p_180434_5_ * f10), (double)(f13 - p_180434_6_ * f10 - p_180434_8_ * f10)).func_187315_a(1.0, 1.0).func_181675_d();
      buffer.func_181662_b((double)(f11 - p_180434_4_ * f10 + p_180434_7_ * f10), (double)(f12 + p_180434_5_ * f10), (double)(f13 - p_180434_6_ * f10 + p_180434_8_ * f10)).func_187315_a(1.0, 0.0).func_181675_d();
      buffer.func_181662_b((double)(f11 + p_180434_4_ * f10 + p_180434_7_ * f10), (double)(f12 + p_180434_5_ * f10), (double)(f13 + p_180434_6_ * f10 + p_180434_8_ * f10)).func_187315_a(0.0, 0.0).func_181675_d();
      buffer.func_181662_b((double)(f11 + p_180434_4_ * f10 - p_180434_7_ * f10), (double)(f12 - p_180434_5_ * f10), (double)(f13 + p_180434_6_ * f10 - p_180434_8_ * f10)).func_187315_a(0.0, 1.0).func_181675_d();
      Tessellator.func_178181_a().func_78381_a();
      Minecraft.func_71410_x().field_71446_o.func_110577_a(particleTextures);
      buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
   }

   public int func_189214_a(float p_70070_1_) {
      return 257;
   }
}

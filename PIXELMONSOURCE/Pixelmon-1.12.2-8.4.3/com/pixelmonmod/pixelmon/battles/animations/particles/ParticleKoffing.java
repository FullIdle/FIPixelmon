package com.pixelmonmod.pixelmon.battles.animations.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleKoffing extends Particle {
   float smokeParticleScale;

   public ParticleKoffing(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      this(par1World, par2, par4, par6, par8, par10, par12, 1.0F);
   }

   public ParticleKoffing(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float par14) {
      super(par1World, par2, par4, par6, 0.0, 0.0, 0.0);
      this.field_187129_i *= 0.10000000149011612;
      this.field_187130_j *= 0.10000000149011612;
      this.field_187131_k *= 0.10000000149011612;
      this.field_187129_i += par8;
      this.field_187130_j += par10;
      this.field_187131_k += par12;
      this.field_70552_h = 0.6F;
      this.field_70553_i = 0.6F;
      this.field_70551_j = 0.48F;
      this.field_70544_f *= 0.75F;
      this.field_70544_f *= par14;
      this.smokeParticleScale = this.field_70544_f;
      this.field_70547_e = (int)(8.0 / (Math.random() * 0.8 + 0.2));
      this.field_70547_e = (int)((float)this.field_70547_e * par14);
   }

   public void func_180434_a(BufferBuilder tessalator, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
      float var8 = ((float)this.field_70546_d + p_180434_3_) / (float)this.field_70547_e * 32.0F;
      if (var8 < 0.0F) {
         var8 = 0.0F;
      }

      if (var8 > 1.0F) {
         var8 = 1.0F;
      }

      this.field_70544_f = this.smokeParticleScale * var8;
      super.func_180434_a(tessalator, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

      this.func_70536_a(7 - this.field_70546_d * 8 / this.field_70547_e);
      this.field_187130_j += 0.004;
      if (this.field_187127_g == this.field_187124_d) {
         this.field_187129_i *= 1.1;
         this.field_187131_k *= 1.1;
      }

      this.field_187129_i *= 0.9599999785423279;
      this.field_187130_j *= 0.9599999785423279;
      this.field_187131_k *= 0.9599999785423279;
      if (this.field_187132_l) {
         this.field_187129_i *= 0.699999988079071;
         this.field_187131_k *= 0.699999988079071;
      }

   }
}

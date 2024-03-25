package com.pixelmonmod.pixelmon.battles.animations.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleGastly extends Particle {
   float field_70569_a;

   public ParticleGastly(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, boolean isShiny) {
      super(par1World, par2, par4, par6, 0.0, 0.0, 0.0);
      float var14 = 3.0F;
      this.field_187129_i *= 0.10000000149011612;
      this.field_187130_j *= 0.10000000149011612;
      this.field_187131_k *= 0.10000000149011612;
      this.field_187129_i += par8;
      this.field_187130_j += par10;
      this.field_187131_k += par12;
      this.field_70552_h = 0.19F;
      this.field_70553_i = 0.0F;
      this.field_70551_j = 0.38F;
      this.field_70544_f *= 0.5F;
      this.field_70544_f *= var14;
      this.field_70569_a = this.field_70544_f;
      this.field_70547_e = (int)(2.0 / (Math.random() * 0.8 + 0.3));
      this.field_70547_e = (int)((float)this.field_70547_e * var14);
      if (isShiny) {
         this.field_70552_h = 0.35F;
         this.field_70553_i = 0.52F;
         this.field_70551_j = 0.68F;
      } else {
         this.field_70552_h = 0.23F;
         this.field_70553_i = 0.13F;
         this.field_70551_j = 0.4F;
      }

   }

   public void func_180434_a(BufferBuilder buffer, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
      float var8 = ((float)this.field_70546_d + p_180434_3_) / (float)this.field_70547_e * 32.0F;
      if (var8 < 0.0F) {
         var8 = 0.0F;
      }

      if (var8 > 1.0F) {
         var8 = 1.0F;
      }

      this.field_70544_f = this.field_70569_a * var8;
      super.func_180434_a(buffer, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

      this.func_70536_a(7 - this.field_70546_d * 8 / this.field_70547_e);
      this.field_187129_i *= 0.9599999785423279;
      this.field_187130_j *= 0.9599999785423279;
      this.field_187131_k *= 0.9599999785423279;
   }
}

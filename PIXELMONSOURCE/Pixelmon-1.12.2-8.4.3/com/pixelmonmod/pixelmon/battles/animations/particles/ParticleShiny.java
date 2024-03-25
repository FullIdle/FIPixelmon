package com.pixelmonmod.pixelmon.battles.animations.particles;

import net.minecraft.client.particle.ParticleExplosion;
import net.minecraft.world.World;

public class ParticleShiny extends ParticleExplosion {
   float smokeParticleScale;

   public ParticleShiny(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      this(par1World, par2, par4, par6, par8, par10, par12, 1.0F);
   }

   public ParticleShiny(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float par14) {
      super(par1World, par2, par4, par6, 0.0, 0.0, 0.0);
      this.field_70552_h = 1.0F;
      this.field_70553_i = 0.8F;
      this.field_70551_j = 0.3F;
      this.field_187129_i = par8 + (double)((float)Math.random()) - 0.5;
      this.field_187130_j = par10 + (double)((float)Math.random() / 2.0F);
      this.field_187131_k = par12 + (double)((float)Math.random()) - 0.5;
      this.field_70544_f = this.field_187136_p.nextFloat() * this.field_187136_p.nextFloat() + 0.7F;
      this.field_70547_e = 10;
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
      this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
      this.field_187129_i *= 0.8999999761581421;
      this.field_187130_j *= 0.8999999761581421;
      this.field_187131_k *= 0.8999999761581421;
      if (this.field_187132_l) {
         this.field_187129_i *= 0.699999988079071;
         this.field_187131_k *= 0.699999988079071;
      }

   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.particleEffects;

import com.pixelmonmod.pixelmon.battles.animations.particles.ParticlePixelmonFlame;
import com.pixelmonmod.pixelmon.battles.animations.particles.ParticleSmoke;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import net.minecraft.client.Minecraft;

public class FlameParticles extends ParticleEffects {
   public float radius;
   public float yOffset;
   private byte count = 0;
   private int size = 0;

   public FlameParticles(float r, float y, int s) {
      this.radius = r;
      this.yOffset = y;
      this.size = s;
   }

   private Double random(double sc, boolean np) {
      if (!np) {
         return Math.random() * sc;
      } else {
         double d = Math.random();
         return d > 0.5 ? Math.random() * sc : -(Math.random() * sc);
      }
   }

   private Double random(double sc) {
      return this.random(sc, true);
   }

   public void onUpdate(Entity2Client pokemon) {
      double x = pokemon.field_70165_t;
      double y = pokemon.field_70163_u + (double)(this.yOffset * pokemon.getScaleFactor());
      double z = pokemon.field_70161_v;
      float f = 180.0F - pokemon.field_70761_aq;
      x += Math.sin(Math.toRadians((double)f)) * (double)this.radius * (double)pokemon.getScaleFactor();
      z += Math.cos(Math.toRadians((double)f)) * (double)this.radius * (double)pokemon.getScaleFactor();
      byte countmax = 3;
      byte var10002 = this.count;
      this.count = (byte)(var10002 + 1);
      if (var10002 == countmax) {
         int i;
         boolean movedMuch;
         if (pokemon.func_70026_G()) {
            for(i = 0; i < this.size * 2; ++i) {
               ParticleSmoke esp = new ParticleSmoke(pokemon.field_70170_p, x, y, z, this.random(0.001 * (double)this.size), this.random(0.001 * (double)this.size, false), this.random(0.001 * (double)this.size));
               movedMuch = Math.abs(pokemon.field_70165_t - pokemon.field_70142_S) > 0.3 || Math.abs(pokemon.field_70163_u - pokemon.field_70137_T) > 0.1 || Math.abs(pokemon.field_70161_v - pokemon.field_70136_U) > 0.1;
               if (movedMuch) {
                  esp.func_187114_a(esp.maxAge() / this.size);
               } else {
                  esp.func_187114_a(esp.maxAge() * this.size / 10);
               }

               Minecraft.func_71410_x().field_71452_i.func_78873_a(esp);
            }
         } else {
            for(i = 0; i < this.size * 2; ++i) {
               ParticlePixelmonFlame efp = new ParticlePixelmonFlame(pokemon.field_70170_p, x, y, z, this.random(0.005 * (double)this.size), this.random(0.015 * (double)this.size, false), this.random(0.005 * (double)this.size));
               movedMuch = Math.abs(pokemon.field_70165_t - pokemon.field_70142_S) > 0.1 || Math.abs(pokemon.field_70163_u - pokemon.field_70137_T) > 0.1 || Math.abs(pokemon.field_70161_v - pokemon.field_70136_U) > 0.1;
               if (pokemon.getSpecies() == EnumSpecies.Charizard) {
                  if (pokemon.getPokemonData().getForm() == 1) {
                     efp.func_70538_b(0.43F, 0.77F, 0.95F);
                     efp.func_70536_a(3);
                  } else if (pokemon.getPokemonData().getFormEnum() == EnumSpecial.Zombie) {
                     efp.func_70538_b(0.63F, 0.26F, 0.64F);
                     efp.func_70536_a(3);
                  }
               }

               if (movedMuch) {
                  efp.func_187114_a(efp.maxAge() / 4);
               } else {
                  efp.func_187114_a(efp.maxAge() * this.size / 10);
               }

               Minecraft.func_71410_x().field_71452_i.func_78873_a(efp);
            }
         }

         this.count = 0;
      }

   }
}

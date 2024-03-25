package com.pixelmonmod.pixelmon.entities.pixelmon.particleEffects;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumPixelmonParticles;
import net.minecraft.util.math.MathHelper;

public class GastlyParticles extends ParticleEffects {
   public void onUpdate(Entity2Client pixelmon) {
      float var2 = 0.6F;
      float var4 = this.rand.nextFloat() * 3.1415927F * 2.0F;
      float var42 = this.rand.nextFloat() * 3.1415927F * 2.0F;
      float var5 = this.rand.nextFloat() * 1.0F + 0.5F;
      float var52 = this.rand.nextFloat() * 1.0F + 0.5F;
      float var6 = MathHelper.func_76126_a(var4) * var2 * 0.7F * var5;
      float var62 = MathHelper.func_76126_a(var42) * var2 * 0.7F * var52;
      float var7 = MathHelper.func_76134_b(var4) * var2 * 0.5F * var5;
      float var72 = MathHelper.func_76134_b(var42) * var2 * 0.5F * var52;
      float var8 = this.rand.nextFloat() * var2 * 1.2F;
      float var82 = this.rand.nextFloat() * var2 * 1.2F;
      int i;
      if (pixelmon.getPokemonData().getGrowth() == EnumGrowth.Enormous) {
         for(i = 0; i < 20; ++i) {
            ClientProxy.spawnParticle(EnumPixelmonParticles.gastly, pixelmon.field_70170_p, pixelmon.field_70165_t + (double)var6 + 0.20000000298023224, pixelmon.field_70163_u + 2.0 + (double)var8, pixelmon.field_70161_v + (double)var7 + 0.4000000059604645, pixelmon.getPokemonData().isShiny());
         }

         for(i = 0; i < 20; ++i) {
            ClientProxy.spawnParticle(EnumPixelmonParticles.gastly, pixelmon.field_70170_p, pixelmon.field_70165_t + (double)var62 + 0.20000000298023224, pixelmon.field_70163_u + 2.0 + (double)var82, pixelmon.field_70161_v + (double)var72 - 0.6000000238418579, pixelmon.getPokemonData().isShiny());
         }
      } else if (pixelmon.getPokemonData().getGrowth() == EnumGrowth.Huge) {
         for(i = 0; i < 20; ++i) {
            ClientProxy.spawnParticle(EnumPixelmonParticles.gastly, pixelmon.field_70170_p, pixelmon.field_70165_t + (double)var6 + 0.20000000298023224, pixelmon.field_70163_u + 1.5 + (double)var8, pixelmon.field_70161_v + (double)var7 + 0.4000000059604645, pixelmon.getPokemonData().isShiny());
         }

         for(i = 0; i < 20; ++i) {
            ClientProxy.spawnParticle(EnumPixelmonParticles.gastly, pixelmon.field_70170_p, pixelmon.field_70165_t + (double)var62 + 0.20000000298023224, pixelmon.field_70163_u + 1.5 + (double)var82, pixelmon.field_70161_v + (double)var72 - 0.6000000238418579, pixelmon.getPokemonData().isShiny());
         }
      } else if (pixelmon.getPokemonData().getGrowth() == EnumGrowth.Pygmy) {
         for(i = 0; i < 20; ++i) {
            ClientProxy.spawnParticle(EnumPixelmonParticles.gastly, pixelmon.field_70170_p, pixelmon.field_70165_t + (double)var6 + 0.20000000298023224, pixelmon.field_70163_u + (double)var8 + 0.6, pixelmon.field_70161_v + (double)var7 + 0.4000000059604645, pixelmon.getPokemonData().isShiny());
         }
      } else {
         for(i = 0; i < 20; ++i) {
            ClientProxy.spawnParticle(EnumPixelmonParticles.gastly, pixelmon.field_70170_p, pixelmon.field_70165_t + (double)var6 + 0.20000000298023224, pixelmon.field_70163_u + 1.2000000476837158 + (double)var8, pixelmon.field_70161_v + (double)var7 + 0.4000000059604645, pixelmon.getPokemonData().isShiny());
         }

         for(i = 0; i < 20; ++i) {
            ClientProxy.spawnParticle(EnumPixelmonParticles.gastly, pixelmon.field_70170_p, pixelmon.field_70165_t + (double)var62 + 0.20000000298023224, pixelmon.field_70163_u + 1.2000000476837158 + (double)var82, pixelmon.field_70161_v + (double)var72 - 0.6000000238418579, pixelmon.getPokemonData().isShiny());
         }
      }

   }
}

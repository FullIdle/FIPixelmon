package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeCondition extends EvoCondition {
   public ArrayList biomes;

   public BiomeCondition() {
      super("biome");
      this.biomes = new ArrayList();
   }

   public BiomeCondition(Biome... biomes) {
      this();
      Biome[] var2 = biomes;
      int var3 = biomes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Biome biome = var2[var4];
         this.biomes.add(biome.getRegistryName().toString());
      }

   }

   public BiomeCondition(ArrayList biomes) {
      this();
      biomes.forEach((b) -> {
         this.biomes.add(b.getRegistryName().toString());
      });
   }

   public boolean passes(EntityPixelmon pixelmon) {
      ResourceLocation biome;
      if (pixelmon.func_70902_q() != null) {
         biome = pixelmon.func_70902_q().func_130014_f_().func_180494_b(pixelmon.func_70902_q().func_180425_c()).getRegistryName();
      } else {
         biome = pixelmon.func_130014_f_().func_180494_b(pixelmon.func_180425_c()).getRegistryName();
      }

      if (biome == null) {
         return false;
      } else {
         return this.biomes.contains(biome.toString()) || this.biomes.contains(biome.func_110623_a());
      }
   }
}

package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import com.google.common.collect.ImmutableList;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpaceBiomeRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Biomes;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class GenLayerBiomeUltraSpace extends GenLayer {
   private List[] biomes = new ArrayList[BiomeType.values().length];
   private final ChunkGeneratorSettings settings;

   public GenLayerBiomeUltraSpace(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, ChunkGeneratorSettings p_i45560_5_) {
      super(p_i45560_1_);
      this.field_75909_a = p_i45560_3_;
      BiomeManager.BiomeType[] var6 = BiomeType.values();
      int warmIdx = var6.length;

      int coolIdx;
      for(coolIdx = 0; coolIdx < warmIdx; ++coolIdx) {
         BiomeManager.BiomeType type = var6[coolIdx];
         ImmutableList biomesToAdd = BiomeManager.getBiomes(type);
         int idx = type.ordinal();
         if (this.biomes[idx] == null) {
            this.biomes[idx] = new ArrayList();
         }

         if (biomesToAdd != null) {
            this.biomes[idx].addAll(biomesToAdd);
         }

         this.biomes[idx].removeIf((entry) -> {
            return entry.biome == Biomes.field_76771_b || entry.biome == Biomes.field_150575_M || entry.biome == Biomes.field_76776_l;
         });
      }

      int desertIdx = BiomeType.DESERT.ordinal();
      this.biomes[desertIdx].add(new BiomeManager.BiomeEntry(Biomes.field_76769_d, 30));
      this.biomes[desertIdx].add(new BiomeManager.BiomeEntry(Biomes.field_150588_X, 20));
      this.biomes[desertIdx].add(new BiomeManager.BiomeEntry(Biomes.field_76772_c, 10));
      this.biomes[desertIdx].add(UltraSpaceBiomeRegistry.ultraDesertEntry);
      warmIdx = BiomeType.WARM.ordinal();
      this.biomes[warmIdx].add(new BiomeManager.BiomeEntry(Biomes.field_185446_X, 20));
      this.biomes[warmIdx].add(UltraSpaceBiomeRegistry.ultraForestEntry);
      this.biomes[warmIdx].add(UltraSpaceBiomeRegistry.ultraJungleEntry);
      coolIdx = BiomeType.COOL.ordinal();
      this.biomes[coolIdx].add(UltraSpaceBiomeRegistry.ultraDeepSeaEntry);
      int icyIdx = BiomeType.ICY.ordinal();
      this.settings = p_i45560_5_;
   }

   public int[] func_75904_a(int areaX, int areaY, int areaWidth, int areaHeight) {
      int[] aint = this.field_75909_a.func_75904_a(areaX, areaY, areaWidth, areaHeight);
      int[] aint1 = IntCache.func_76445_a(areaWidth * areaHeight);

      for(int i = 0; i < areaHeight; ++i) {
         for(int j = 0; j < areaWidth; ++j) {
            this.func_75903_a((long)(j + areaX), (long)(i + areaY));
            int k = aint[j + i * areaWidth];
            int l = (k & 3840) >> 8;
            k &= -3841;
            if (this.settings != null && this.settings.field_177779_F >= 0) {
               aint1[j + i * areaWidth] = this.settings.field_177779_F;
            } else if (func_151618_b(k)) {
               aint1[j + i * areaWidth] = Biome.func_185362_a(Biomes.field_76785_t);
            } else if (k == Biome.func_185362_a(Biomes.field_76789_p)) {
               aint1[j + i * areaWidth] = k;
            } else if (k == 1) {
               if (l > 0) {
                  if (this.func_75902_a(3) == 0) {
                     aint1[j + i * areaWidth] = Biome.func_185362_a(Biomes.field_150608_ab);
                  } else {
                     aint1[j + i * areaWidth] = Biome.func_185362_a(Biomes.field_150607_aa);
                  }
               } else {
                  aint1[j + i * areaWidth] = Biome.func_185362_a(this.getWeightedBiomeEntry(BiomeType.DESERT).biome);
               }
            } else if (k == 2) {
               if (l > 0) {
                  aint1[j + i * areaWidth] = Biome.func_185362_a(Biomes.field_76782_w);
               } else {
                  aint1[j + i * areaWidth] = Biome.func_185362_a(this.getWeightedBiomeEntry(BiomeType.WARM).biome);
               }
            } else if (k == 3) {
               if (l > 0) {
                  aint1[j + i * areaWidth] = Biome.func_185362_a(Biomes.field_150578_U);
               } else {
                  aint1[j + i * areaWidth] = Biome.func_185362_a(this.getWeightedBiomeEntry(BiomeType.COOL).biome);
               }
            } else if (k == 4) {
               aint1[j + i * areaWidth] = Biome.func_185362_a(this.getWeightedBiomeEntry(BiomeType.ICY).biome);
            } else {
               aint1[j + i * areaWidth] = Biome.func_185362_a(Biomes.field_76789_p);
            }
         }
      }

      return aint1;
   }

   protected BiomeManager.BiomeEntry getWeightedBiomeEntry(BiomeManager.BiomeType type) {
      BiomeManager.BiomeEntry entry;
      do {
         List biomeList = this.biomes[type.ordinal()];
         int totalWeight = WeightedRandom.func_76272_a(biomeList);
         int weight = BiomeManager.isTypeListModded(type) ? this.func_75902_a(totalWeight) : this.func_75902_a(totalWeight / 10) * 10;
         entry = (BiomeManager.BiomeEntry)WeightedRandom.func_180166_a(biomeList, weight);
      } while(entry.biome == Biomes.field_76771_b || entry.biome == Biomes.field_150575_M || entry.biome == Biomes.field_76776_l);

      return entry;
   }
}

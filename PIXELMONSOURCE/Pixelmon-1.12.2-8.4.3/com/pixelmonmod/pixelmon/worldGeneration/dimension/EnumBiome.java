package com.pixelmonmod.pixelmon.worldGeneration.dimension;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpaceBiomeRegistry;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public enum EnumBiome {
   DEFAULT(Biomes.field_180279_ad),
   PLAINS(Biomes.field_76772_c),
   DESERT(Biomes.field_76769_d),
   EXTREME_HILLS(Biomes.field_76770_e),
   FOREST(Biomes.field_76767_f),
   TAIGA(Biomes.field_76768_g),
   SWAMPLAND(Biomes.field_76780_h),
   RIVER(Biomes.field_76781_i),
   HELL(Biomes.field_76778_j),
   SKY(Biomes.field_76779_k),
   FROZEN_RIVER(Biomes.field_76777_m),
   ICE_PLAINS(Biomes.field_76774_n),
   ICE_MOUNTAINS(Biomes.field_76775_o),
   MUSHROOM_ISLAND(Biomes.field_76789_p),
   MUSHROOM_ISLAND_SHORE(Biomes.field_76788_q),
   BEACH(Biomes.field_76787_r),
   DESERT_HILLS(Biomes.field_76786_s),
   FOREST_HILLS(Biomes.field_76785_t),
   TAIGA_HILLS(Biomes.field_76784_u),
   EXTREME_HILLS_EDGE(Biomes.field_76783_v),
   JUNGLE(Biomes.field_76782_w),
   JUNGLE_HILLS(Biomes.field_76792_x),
   JUNGLE_EDGE(Biomes.field_150574_L),
   STONE_BEACH(Biomes.field_150576_N),
   COLD_BEACH(Biomes.field_150577_O),
   BIRCH_FOREST(Biomes.field_150583_P),
   BIRCH_FOREST_HILLS(Biomes.field_150582_Q),
   ROOFED_FOREST(Biomes.field_150585_R),
   COLD_TAIGA(Biomes.field_150584_S),
   COLD_TAIGA_HILLS(Biomes.field_150579_T),
   REDWOOD_TAIGA(Biomes.field_150578_U),
   REDWOOD_TAIGA_HILLS(Biomes.field_150581_V),
   EXTREME_HILLS_WITH_TREES(Biomes.field_150580_W),
   SAVANNA(Biomes.field_150588_X),
   SAVANNA_PLATEAU(Biomes.field_150587_Y),
   MESA(Biomes.field_150589_Z),
   MESA_ROCK(Biomes.field_150607_aa),
   MESA_CLEAR_ROCK(Biomes.field_150608_ab),
   MUTATED_PLAINS(Biomes.field_185441_Q),
   MUTATED_DESERT(Biomes.field_185442_R),
   MUTATED_EXTREME_HILLS(Biomes.field_185443_S),
   MUTATED_FOREST(Biomes.field_185444_T),
   MUTATED_TAIGA(Biomes.field_150590_f),
   MUTATED_SWAMPLAND(Biomes.field_150599_m),
   MUTATED_ICE_FLATS(Biomes.field_185445_W),
   MUTATED_JUNGLE(Biomes.field_185446_X),
   MUTATED_JUNGLE_EDGE(Biomes.field_185447_Y),
   MUTATED_BIRCH_FOREST(Biomes.field_185448_Z),
   MUTATED_BIRCH_FOREST_HILLS(Biomes.field_185429_aa),
   MUTATED_ROOFED_FOREST(Biomes.field_185430_ab),
   MUTATED_TAIGA_COLD(Biomes.field_185431_ac),
   MUTATED_REDWOOD_TAIGA(Biomes.field_185432_ad),
   MUTATED_REDWOOD_TAIGA_HILLS(Biomes.field_185433_ae),
   MUTATED_EXTREME_HILLS_WITH_TREES(Biomes.field_185434_af),
   MUTATED_SAVANNA(Biomes.field_185435_ag),
   MUTATED_SAVANNA_ROCK(Biomes.field_185436_ah),
   MUTATED_MESA(Biomes.field_185437_ai),
   MUTATED_MESA_ROCK(Biomes.field_185438_aj),
   MUTATED_MESA_CLEAR_ROCK(Biomes.field_185439_ak),
   ULTRA_FOREST(UltraSpaceBiomeRegistry.ultraForest),
   ULTRA_JUNGLE(UltraSpaceBiomeRegistry.ultraJungle),
   ULTRA_DEEP_SEA(UltraSpaceBiomeRegistry.ultraDeepSea),
   ULTRA_DESERT(UltraSpaceBiomeRegistry.ultraDesert);

   private Biome biome;

   private EnumBiome(Biome biome) {
      this.biome = biome;
   }

   public static Biome[] getBiomes() {
      Biome[] biomes = new Biome[values().length];

      for(int i = 0; i < values().length; ++i) {
         biomes[i] = values()[i].biome;
      }

      return biomes;
   }
}

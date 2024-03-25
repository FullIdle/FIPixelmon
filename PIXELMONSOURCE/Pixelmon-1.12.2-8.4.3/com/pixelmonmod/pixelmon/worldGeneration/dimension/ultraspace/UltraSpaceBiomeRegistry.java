package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.biomes.UltraDeepSea;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.biomes.UltraDesert;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.biomes.UltraForest;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.biomes.UltraJungle;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.GenLayerEdge.Mode;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UltraSpaceBiomeRegistry {
   public static Biome ultraForest;
   public static Biome ultraJungle;
   public static Biome ultraDeepSea;
   public static Biome ultraDesert;
   public static BiomeManager.BiomeEntry ultraForestEntry;
   public static BiomeManager.BiomeEntry ultraJungleEntry;
   public static BiomeManager.BiomeEntry ultraDeepSeaEntry;
   public static BiomeManager.BiomeEntry ultraDesertEntry;

   @SubscribeEvent
   public void registerBiomes(RegistryEvent.Register event) {
      ultraForest = (Biome)(new UltraForest((new Biome.BiomeProperties("Ultra Forest")).func_185398_c(0.25F).func_185400_d(0.3F).func_185410_a(0.7F).func_185395_b(0.5F))).setRegistryName("pixelmon", "ultra_forest");
      ultraJungle = (Biome)(new UltraJungle((new Biome.BiomeProperties("Ultra Jungle")).func_185398_c(0.45F).func_185400_d(0.3F).func_185410_a(0.95F).func_185395_b(0.9F))).setRegistryName("pixelmon", "ultra_jungle");
      ultraDeepSea = (Biome)(new UltraDeepSea((new Biome.BiomeProperties("Ultra Deep Sea")).func_185398_c(0.25F).func_185400_d(0.8F).func_185410_a(0.3F).func_185395_b(0.0F).func_185396_a())).setRegistryName("pixelmon", "ultra_deep_sea");
      ultraDesert = (Biome)(new UltraDesert((new Biome.BiomeProperties("Ultra Desert")).func_185398_c(0.45F).func_185400_d(0.3F).func_185410_a(2.0F).func_185395_b(0.0F).func_185396_a())).setRegistryName("pixelmon", "ultra_desert");
      ultraForestEntry = new BiomeManager.BiomeEntry(ultraForest, 10);
      ultraJungleEntry = new BiomeManager.BiomeEntry(ultraJungle, 10);
      ultraDeepSeaEntry = new BiomeManager.BiomeEntry(ultraDeepSea, 10);
      ultraDesertEntry = new BiomeManager.BiomeEntry(ultraDesert, 10);
      event.getRegistry().register(ultraForest);
      event.getRegistry().register(ultraJungle);
      event.getRegistry().register(ultraDeepSea);
      event.getRegistry().register(ultraDesert);
   }

   public static GenLayer[] initUltraSpaceBiomeGenerators(long seed, WorldType p_180781_2_, ChunkGeneratorSettings p_180781_3_) {
      GenLayer genlayer = new GenLayerIsland(1L);
      GenLayer genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
      GenLayer genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
      GenLayer genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
      GenLayer genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
      genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
      genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
      GenLayer genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
      GenLayer genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
      GenLayer genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
      GenLayer genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, Mode.COOL_WARM);
      genlayeredge = new GenLayerEdge(2L, genlayeredge, Mode.HEAT_ICE);
      genlayeredge = new GenLayerEdge(3L, genlayeredge, Mode.SPECIAL);
      GenLayer genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
      genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
      GenLayer genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
      GenLayer genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
      GenLayer genlayer4 = GenLayerZoom.func_75915_a(1000L, genlayeraddmushroomisland, 0);
      int i = 3;
      int j = i;
      if (p_180781_3_ != null) {
         i = p_180781_3_.field_177780_G;
         j = p_180781_3_.field_177788_H;
      }

      GenLayer lvt_7_1_ = GenLayerZoom.func_75915_a(1000L, genlayer4, 0);
      GenLayer genlayerriverinit = new GenLayerRiverInit(100L, lvt_7_1_);
      GenLayer genlayerbiomeedge = p_180781_2_.getBiomeLayer(seed, genlayer4, p_180781_3_);
      GenLayer lvt_9_1_ = GenLayerZoom.func_75915_a(1000L, genlayerriverinit, 2);
      GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_9_1_);
      GenLayer genlayer5 = GenLayerZoom.func_75915_a(1000L, genlayerriverinit, 2);
      genlayer5 = GenLayerZoom.func_75915_a(1000L, genlayer5, j);
      GenLayer genlayerriver = new GenLayerRiver(1L, genlayer5);
      GenLayer genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
      GenLayer genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);

      for(int k = 0; k < i; ++k) {
         genlayerhills = new GenLayerZoom((long)(1000 + k), (GenLayer)genlayerhills);
         if (k == 0) {
            genlayerhills = new GenLayerAddIsland(3L, (GenLayer)genlayerhills);
         }
      }

      GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)genlayerhills);
      GenLayer genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
      GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
      genlayerrivermix.func_75905_a(seed);
      genlayer3.func_75905_a(seed);
      return new GenLayer[]{genlayerrivermix, genlayer3, genlayerrivermix};
   }
}

package com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.biomes.InfiniteOcean;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DrownedWorldBiomeRegistry {
   public static Biome infiniteOcean;
   public static BiomeManager.BiomeEntry infiniteOceanEntry;

   @SubscribeEvent
   public void registerBiomes(RegistryEvent.Register event) {
      infiniteOcean = (Biome)(new InfiniteOcean((new Biome.BiomeProperties("Infinite Ocean")).func_185398_c(0.25F).func_185400_d(0.0F).func_185410_a(1.0F).func_185395_b(0.0F))).setRegistryName("pixelmon", "infinite_ocean");
      infiniteOceanEntry = new BiomeManager.BiomeEntry(infiniteOcean, 10);
      event.getRegistry().register(infiniteOcean);
   }
}

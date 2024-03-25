package com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.BiomeDictionary.Type;

public class DrownedWorld {
   public static int DIM_ID;
   public static final String DIM_NAME = "drowned_world";
   public static DimensionType DIM_TYPE;
   public static final WorldType WORLD_TYPE = new DrownedWorldWorldType();

   public static void register() {
      if (PixelmonConfig.drownedWorldDimId < 2) {
         DIM_ID = 73;
      } else {
         DIM_ID = PixelmonConfig.drownedWorldDimId;
      }

      DIM_TYPE = DimensionType.register("drowned_world", "drowned", DIM_ID, DrownedWorldWorldProvider.class, false);
      DimensionManager.registerDimension(DIM_ID, DIM_TYPE);
   }

   public static void registerWorldFeatures() {
      MinecraftForge.EVENT_BUS.register(new DrownedWorldBiomeRegistry());
   }

   public static void setupBiomes() {
      BiomeDictionary.addTypes(DrownedWorldBiomeRegistry.infiniteOcean, new BiomeDictionary.Type[]{Type.OCEAN});
   }
}

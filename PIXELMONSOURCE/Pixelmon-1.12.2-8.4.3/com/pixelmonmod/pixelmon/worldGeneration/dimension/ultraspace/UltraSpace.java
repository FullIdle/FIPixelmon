package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.MapGenEndCityUltraSpace;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.BiomeDictionary.Type;

public class UltraSpace {
   public static int DIM_ID;
   public static final String DIM_NAME = "ultra_space";
   public static DimensionType DIM_TYPE;
   public static final WorldType WORLD_TYPE = new UltraSpaceWorldType();

   public static void register() {
      if (PixelmonConfig.ultraSpaceDimId < 2) {
         DIM_ID = 72;
      } else {
         DIM_ID = PixelmonConfig.ultraSpaceDimId;
      }

      DIM_TYPE = DimensionType.register("ultra_space", "ultra", DIM_ID, UltraSpaceWorldProvider.class, false);
      DimensionManager.registerDimension(DIM_ID, DIM_TYPE);
   }

   public static void registerWorldFeatures() {
      MinecraftForge.EVENT_BUS.register(new UltraSpaceBiomeRegistry());
      MapGenStructureIO.func_143034_b(MapGenEndCityUltraSpace.Start.class, "EndCityU");
   }

   public static void setupBiomes() {
      BiomeDictionary.addTypes(UltraSpaceBiomeRegistry.ultraForest, new BiomeDictionary.Type[]{Type.FOREST, Type.LUSH});
      BiomeDictionary.addTypes(UltraSpaceBiomeRegistry.ultraJungle, new BiomeDictionary.Type[]{Type.JUNGLE, Type.LUSH, Type.WET, Type.HOT});
      BiomeDictionary.addTypes(UltraSpaceBiomeRegistry.ultraDeepSea, new BiomeDictionary.Type[]{Type.MOUNTAIN, Type.COLD, Type.MAGICAL, Type.DRY});
      BiomeDictionary.addTypes(UltraSpaceBiomeRegistry.ultraDesert, new BiomeDictionary.Type[]{Type.SANDY, Type.HOT, Type.DRY});
   }
}

package com.pixelmonmod.pixelmon.worldGeneration.dimension.debug;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;

public class DebugWorldRegistry {
   public static String DIM_NAME = "pixelmondebug";
   public static DimensionType DIM_TYPE;
   public static final WorldType WORLD_TYPE = new DebugWorldType();

   public static void init() {
      DIM_TYPE = DimensionType.register(DIM_NAME, "debug", -99, DebugWorldProvider.class, false);
   }
}

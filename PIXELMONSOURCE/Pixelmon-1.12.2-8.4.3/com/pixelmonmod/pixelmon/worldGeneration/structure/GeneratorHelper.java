package com.pixelmonmod.pixelmon.worldGeneration.structure;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpace;
import net.minecraft.world.World;

public class GeneratorHelper {
   public static boolean isOverworld(World w) {
      int dimensionID = w.field_73011_w.getDimension();
      return dimensionID == 0;
   }

   public static boolean isUltraSpace(World w) {
      int dimensionID = w.field_73011_w.getDimension();
      return dimensionID == UltraSpace.DIM_ID;
   }
}

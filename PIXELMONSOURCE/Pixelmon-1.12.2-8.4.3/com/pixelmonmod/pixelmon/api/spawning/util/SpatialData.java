package com.pixelmonmod.pixelmon.api.spawning.util;

import java.util.Set;
import net.minecraft.block.Block;

public class SpatialData {
   public int radius = 0;
   public Block baseBlock;
   public Set uniqueSurroundingBlocks;

   public SpatialData(int radius, Block baseBlock, Set uniqueSurroundingBlocks) {
      this.radius = radius;
      this.baseBlock = baseBlock;
      this.uniqueSurroundingBlocks = uniqueSurroundingBlocks;
   }
}

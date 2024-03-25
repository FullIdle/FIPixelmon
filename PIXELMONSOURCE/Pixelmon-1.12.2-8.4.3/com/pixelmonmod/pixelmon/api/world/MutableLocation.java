package com.pixelmonmod.pixelmon.api.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MutableLocation {
   public World world;
   public BlockPos.MutableBlockPos pos;

   public MutableLocation(World world, BlockPos pos) {
      this.world = world;
      this.pos = new BlockPos.MutableBlockPos(pos);
   }

   public MutableLocation(World world, int x, int y, int z) {
      this.world = world;
      this.pos = new BlockPos.MutableBlockPos(x, y, z);
   }
}

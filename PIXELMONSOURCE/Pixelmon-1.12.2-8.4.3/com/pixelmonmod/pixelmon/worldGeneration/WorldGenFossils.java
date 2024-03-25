package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenFossils implements IWorldGenerator {
   public boolean generate(World world, Random rand, int x, int y, int z) {
      BlockPos pos = new BlockPos(x, y, z);
      if (world.func_180495_p(pos).func_177230_c() == Blocks.field_150351_n) {
         world.func_180501_a(pos, PixelmonBlocks.fossil.func_176223_P(), 18);
         return true;
      } else {
         return false;
      }
   }

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      for(int i = 0; i < 6; ++i) {
         int xPos = random.nextInt(16) + chunkX * 16;
         int zPos = random.nextInt(16) + chunkZ * 16;
         int yPos = random.nextInt(30) + 20;
         if (this.generate(world, random, xPos, yPos - random.nextInt(9), zPos)) {
            break;
         }
      }

   }
}

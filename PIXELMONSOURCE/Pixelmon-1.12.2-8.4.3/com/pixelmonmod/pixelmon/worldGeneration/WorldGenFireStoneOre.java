package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenFireStoneOre extends WorldGenerator implements IWorldGenerator {
   public boolean func_180709_b(World world, Random rand, BlockPos pos) {
      int y = this.findTopLavaBlock(world, pos);
      ++y;
      pos = new BlockPos(pos.func_177958_n(), y, pos.func_177952_p());
      if (world.func_180495_p(pos).func_185904_a() == Material.field_151579_a) {
         for(int i = 1; i < 10; ++i) {
            ++y;
            pos = new BlockPos(pos.func_177958_n(), y, pos.func_177952_p());
            if (world.func_180495_p(pos).func_185904_a() != Material.field_151579_a) {
               world.func_180501_a(pos, PixelmonBlocks.fireStoneOre.func_176223_P(), 18);
               return true;
            }
         }
      }

      return false;
   }

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      for(int i = 0; i < 30; ++i) {
         int xPos = random.nextInt(16) + chunkX * 16;
         int zPos = random.nextInt(16) + chunkZ * 16;
         if (this.hasLava(world, new BlockPos(xPos, 0, zPos))) {
            this.func_180709_b(world, random, new BlockPos(xPos, 0, zPos));
         }
      }

   }

   private boolean hasLava(World world, BlockPos pos) {
      for(int i = 0; i < 30; ++i) {
         if (world.func_180495_p(new BlockPos(pos.func_177958_n(), i, pos.func_177952_p())).func_177230_c() == Blocks.field_150353_l) {
            return true;
         }
      }

      return false;
   }

   private int findTopLavaBlock(World world, BlockPos pos) {
      for(int i = 0; i < 30; ++i) {
         if (world.func_180495_p(new BlockPos(pos.func_177958_n(), i, pos.func_177952_p())).func_177230_c() == Blocks.field_150353_l) {
            return i;
         }
      }

      return -1;
   }
}

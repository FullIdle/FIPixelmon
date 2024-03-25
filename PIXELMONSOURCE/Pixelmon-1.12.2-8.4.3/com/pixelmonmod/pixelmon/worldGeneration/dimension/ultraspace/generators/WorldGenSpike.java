package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenSpike extends WorldGenAbstractTree {
   private IBlockState BLOCK;
   private IBlockState UNCOMMON;
   private IBlockState RARE;

   public WorldGenSpike(IBlockState block, IBlockState uncommon, IBlockState rare) {
      super(false);
      this.BLOCK = block;
      this.UNCOMMON = uncommon;
      this.RARE = rare;
   }

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      int height = rand.nextInt(4) + rand.nextInt(4) + 2;
      if (position.func_177956_o() >= 1 && position.func_177956_o() + height + 1 <= 256) {
         BlockPos down = position.func_177977_b();
         if (worldIn.func_175623_d(position) && !worldIn.func_175623_d(down)) {
            for(int i = 0; i < height; ++i) {
               double seed = rand.nextDouble();
               if (seed < 5.0E-4) {
                  this.func_175903_a(worldIn, position.func_177982_a(0, i, 0), this.RARE);
               } else if (seed < 0.005) {
                  this.func_175903_a(worldIn, position.func_177982_a(0, i, 0), this.UNCOMMON);
               } else {
                  this.func_175903_a(worldIn, position.func_177982_a(0, i, 0), this.BLOCK);
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }
}

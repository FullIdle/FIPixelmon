package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenPyramid extends WorldGenAbstractTree {
   private IBlockState BLOCK;
   private IBlockState UNCOMMON;
   private IBlockState RARE;

   public WorldGenPyramid(IBlockState block, IBlockState uncommon, IBlockState rare) {
      super(false);
      this.BLOCK = block;
      this.UNCOMMON = uncommon;
      this.RARE = rare;
   }

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      int tiers = rand.nextInt(5) + 3 + rand.nextInt(2);
      if (position.func_177956_o() >= 1 && position.func_177956_o() + tiers + 1 <= 256) {
         BlockPos down = position.func_177977_b();
         if (worldIn.func_175623_d(position) && !worldIn.func_175623_d(down)) {
            int y = 0;

            while(true) {
               if (y >= tiers) {
                  if (tiers >= 5) {
                     double rare = rand.nextDouble();
                     if (rare < 0.01) {
                        this.func_175903_a(worldIn, position.func_177982_a(0, 3, 0), this.RARE);
                     } else if (rare < 0.05) {
                        this.func_175903_a(worldIn, position.func_177982_a(0, 3, 0), this.UNCOMMON);
                     }
                  }
                  break;
               }

               int width = (tiers - y) * 2 - 1;
               int sizing = (width - 1) / 2;

               for(int w = 0; w < width; ++w) {
                  if (y == 0 && (worldIn.func_175623_d(position.func_177982_a(sizing, -1, sizing)) || worldIn.func_175623_d(position.func_177982_a(sizing, -1, -sizing)) || worldIn.func_175623_d(position.func_177982_a(-sizing, -1, sizing)) || worldIn.func_175623_d(position.func_177982_a(-sizing, -1, -sizing)))) {
                     return false;
                  }

                  for(int x = -sizing; x <= sizing; ++x) {
                     for(int z = -sizing; z <= sizing; ++z) {
                        this.func_175903_a(worldIn, position.func_177982_a(x, y, z), this.BLOCK);
                     }
                  }
               }

               ++y;
            }
         }

         return true;
      } else {
         return false;
      }
   }
}

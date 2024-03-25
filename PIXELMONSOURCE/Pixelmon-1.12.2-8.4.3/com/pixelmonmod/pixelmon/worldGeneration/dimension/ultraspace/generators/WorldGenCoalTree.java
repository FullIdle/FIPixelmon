package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import java.util.Random;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenCoalTree extends WorldGenAbstractTree {
   private static final IBlockState TRUNK;
   private static final IBlockState LEAFA;
   private static final IBlockState LEAFB;

   public WorldGenCoalTree(boolean notify) {
      super(notify);
   }

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      int i = rand.nextInt(3) + 5;
      i += rand.nextInt(3);
      boolean flag = true;
      if (position.func_177956_o() >= 1 && position.func_177956_o() + i + 1 <= 256) {
         int i2;
         int k2;
         for(int j = position.func_177956_o(); j <= position.func_177956_o() + 1 + i; ++j) {
            int k = 1;
            if (j == position.func_177956_o()) {
               k = 0;
            }

            if (j >= position.func_177956_o() + 1 + i - 2) {
               k = 2;
            }

            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(i2 = position.func_177958_n() - k; i2 <= position.func_177958_n() + k && flag; ++i2) {
               for(k2 = position.func_177952_p() - k; k2 <= position.func_177952_p() + k && flag; ++k2) {
                  if (j >= 0 && j < worldIn.func_72800_K()) {
                     if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.func_181079_c(i2, j, k2))) {
                        flag = false;
                     }
                  } else {
                     flag = false;
                  }
               }
            }
         }

         if (!flag) {
            return false;
         } else {
            BlockPos down = position.func_177977_b();
            IBlockState state = worldIn.func_180495_p(down);
            boolean isSoil = true;
            if (isSoil && position.func_177956_o() < worldIn.func_72800_K() - i - 1) {
               state.func_177230_c().onPlantGrow(state, worldIn, down, position);

               for(i2 = position.func_177956_o() - 3 + i; i2 <= position.func_177956_o() + i; ++i2) {
                  k2 = i2 - (position.func_177956_o() + i);
                  int l2 = 1 - k2 / 2;

                  for(int i3 = position.func_177958_n() - l2; i3 <= position.func_177958_n() + l2; ++i3) {
                     int j1 = i3 - position.func_177958_n();

                     for(int k1 = position.func_177952_p() - l2; k1 <= position.func_177952_p() + l2; ++k1) {
                        int l1 = k1 - position.func_177952_p();
                        if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                           BlockPos blockpos = new BlockPos(i3, i2, k1);
                           IBlockState state2 = worldIn.func_180495_p(blockpos);
                           if (state2.func_177230_c().isAir(state2, worldIn, blockpos) || state2.func_177230_c().isAir(state2, worldIn, blockpos)) {
                              this.func_175903_a(worldIn, blockpos, rand.nextInt(3) == 0 ? LEAFA : LEAFB);
                           }
                        }
                     }
                  }
               }

               for(i2 = 0; i2 < i; ++i2) {
                  BlockPos upN = position.func_177981_b(i2);
                  IBlockState state2 = worldIn.func_180495_p(upN);
                  if (state2.func_177230_c().isAir(state2, worldIn, upN) || state2.func_177230_c().isLeaves(state2, worldIn, upN)) {
                     this.func_175903_a(worldIn, position.func_177981_b(i2), TRUNK);
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   static {
      TRUNK = Blocks.field_150402_ci.func_176223_P();
      LEAFA = Blocks.field_150399_cn.func_176223_P().func_177226_a(BlockStainedGlass.field_176547_a, EnumDyeColor.PURPLE);
      LEAFB = Blocks.field_150399_cn.func_176223_P().func_177226_a(BlockStainedGlass.field_176547_a, EnumDyeColor.GRAY);
   }
}

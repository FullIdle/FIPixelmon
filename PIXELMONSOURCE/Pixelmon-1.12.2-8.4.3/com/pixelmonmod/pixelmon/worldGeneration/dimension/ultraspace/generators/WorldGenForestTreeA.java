package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import java.util.Random;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenForestTreeA extends WorldGenAbstractTree {
   private static final IBlockState TRUNK;
   private static final IBlockState LEAF;

   public WorldGenForestTreeA() {
      super(false);
   }

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      int i = rand.nextInt(3) + rand.nextInt(3) + 5;
      boolean flag = true;
      if (position.func_177956_o() >= 1 && position.func_177956_o() + i + 1 <= 256) {
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

            for(int l = position.func_177958_n() - k; l <= position.func_177958_n() + k && flag; ++l) {
               for(k2 = position.func_177952_p() - k; k2 <= position.func_177952_p() + k && flag; ++k2) {
                  if (j >= 0 && j < 256) {
                     if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.func_181079_c(l, j, k2))) {
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
            boolean isSoil = state.func_177230_c().canSustainPlant(state, worldIn, down, EnumFacing.UP, (BlockSapling)Blocks.field_150345_g);
            if (isSoil && position.func_177956_o() < worldIn.func_72800_K() - i - 1) {
               state.func_177230_c().onPlantGrow(state, worldIn, down, position);
               EnumFacing enumfacing = Plane.HORIZONTAL.func_179518_a(rand);
               k2 = i - rand.nextInt(4) - 1;
               int l2 = 3 - rand.nextInt(3);
               int i3 = position.func_177958_n();
               int j1 = position.func_177952_p();
               int k1 = 0;

               int k3;
               for(int l1 = 0; l1 < i; ++l1) {
                  k3 = position.func_177956_o() + l1;
                  if (l1 >= k2 && l2 > 0) {
                     i3 += enumfacing.func_82601_c();
                     j1 += enumfacing.func_82599_e();
                     --l2;
                  }

                  BlockPos blockpos = new BlockPos(i3, k3, j1);
                  state = worldIn.func_180495_p(blockpos);
                  if (state.func_177230_c().isAir(state, worldIn, blockpos) || state.func_177230_c().isLeaves(state, worldIn, blockpos)) {
                     this.placeLogAt(worldIn, blockpos);
                     k1 = k3;
                  }
               }

               BlockPos blockpos2 = new BlockPos(i3, k1, j1);

               int l3;
               for(k3 = -3; k3 <= 3; ++k3) {
                  for(l3 = -3; l3 <= 3; ++l3) {
                     if (Math.abs(k3) != 3 || Math.abs(l3) != 3) {
                        this.placeLeafAt(worldIn, blockpos2.func_177982_a(k3, 0, l3));
                     }
                  }
               }

               blockpos2 = blockpos2.func_177984_a();

               for(k3 = -1; k3 <= 1; ++k3) {
                  for(l3 = -1; l3 <= 1; ++l3) {
                     this.placeLeafAt(worldIn, blockpos2.func_177982_a(k3, 0, l3));
                  }
               }

               this.placeLeafAt(worldIn, blockpos2.func_177965_g(2));
               this.placeLeafAt(worldIn, blockpos2.func_177985_f(2));
               this.placeLeafAt(worldIn, blockpos2.func_177970_e(2));
               this.placeLeafAt(worldIn, blockpos2.func_177964_d(2));
               i3 = position.func_177958_n();
               j1 = position.func_177952_p();
               EnumFacing enumfacing1 = Plane.HORIZONTAL.func_179518_a(rand);
               if (enumfacing1 != enumfacing) {
                  l3 = k2 - rand.nextInt(2) - 1;
                  int k4 = 1 + rand.nextInt(3);
                  k1 = 0;

                  int j5;
                  for(int l4 = l3; l4 < i && k4 > 0; --k4) {
                     if (l4 >= 1) {
                        j5 = position.func_177956_o() + l4;
                        i3 += enumfacing1.func_82601_c();
                        j1 += enumfacing1.func_82599_e();
                        BlockPos blockpos1 = new BlockPos(i3, j5, j1);
                        state = worldIn.func_180495_p(blockpos1);
                        if (state.func_177230_c().isAir(state, worldIn, blockpos1) || state.func_177230_c().isLeaves(state, worldIn, blockpos1)) {
                           this.placeLogAt(worldIn, blockpos1);
                           k1 = j5;
                        }
                     }

                     ++l4;
                  }

                  if (k1 > 0) {
                     BlockPos blockpos3 = new BlockPos(i3, k1, j1);

                     int l5;
                     for(j5 = -2; j5 <= 2; ++j5) {
                        for(l5 = -2; l5 <= 2; ++l5) {
                           if (Math.abs(j5) != 2 || Math.abs(l5) != 2) {
                              this.placeLeafAt(worldIn, blockpos3.func_177982_a(j5, 0, l5));
                           }
                        }
                     }

                     blockpos3 = blockpos3.func_177984_a();

                     for(j5 = -1; j5 <= 1; ++j5) {
                        for(l5 = -1; l5 <= 1; ++l5) {
                           this.placeLeafAt(worldIn, blockpos3.func_177982_a(j5, 0, l5));
                        }
                     }
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

   private void placeLogAt(World worldIn, BlockPos pos) {
      this.func_175903_a(worldIn, pos, TRUNK);
   }

   private void placeLeafAt(World worldIn, BlockPos pos) {
      IBlockState state = worldIn.func_180495_p(pos);
      if (state.func_177230_c().isAir(state, worldIn, pos) || state.func_177230_c().isLeaves(state, worldIn, pos)) {
         this.func_175903_a(worldIn, pos, LEAF);
      }

   }

   static {
      TRUNK = Blocks.field_150364_r.func_176223_P();
      LEAF = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockLeaves.field_176236_b, false);
   }
}

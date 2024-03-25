package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenForestTreeB extends WorldGenAbstractTree {
   private Random rand;
   private World world;
   private BlockPos basePos;
   int heightLimit;
   int height;
   double heightAttenuation;
   double branchSlope;
   double scaleWidth;
   double leafDensity;
   int trunkSize;
   int heightLimitLimit;
   int leafDistanceLimit;
   List foliageCoords;

   public WorldGenForestTreeB() {
      super(false);
      this.basePos = BlockPos.field_177992_a;
      this.heightAttenuation = 0.618;
      this.branchSlope = 0.381;
      this.scaleWidth = 1.0;
      this.leafDensity = 1.0;
      this.trunkSize = 1;
      this.heightLimitLimit = 12;
      this.leafDistanceLimit = 4;
   }

   void generateLeafNodeList() {
      this.height = (int)((double)this.heightLimit * this.heightAttenuation);
      if (this.height >= this.heightLimit) {
         this.height = this.heightLimit - 1;
      }

      int i = (int)(1.382 + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0, 2.0));
      if (i < 1) {
         i = 1;
      }

      int j = this.basePos.func_177956_o() + this.height;
      int k = this.heightLimit - this.leafDistanceLimit;
      this.foliageCoords = Lists.newArrayList();
      this.foliageCoords.add(new FoliageCoordinates(this.basePos.func_177981_b(k), j));

      for(; k >= 0; --k) {
         float f = this.layerSize(k);
         if (f >= 0.0F) {
            for(int l = 0; l < i; ++l) {
               double d0 = this.scaleWidth * (double)f * ((double)this.rand.nextFloat() + 0.328);
               double d1 = (double)(this.rand.nextFloat() * 2.0F) * Math.PI;
               double d2 = d0 * Math.sin(d1) + 0.5;
               double d3 = d0 * Math.cos(d1) + 0.5;
               BlockPos blockpos = this.basePos.func_177963_a(d2, (double)(k - 1), d3);
               BlockPos blockpos1 = blockpos.func_177981_b(this.leafDistanceLimit);
               if (this.checkBlockLine(blockpos, blockpos1) == -1) {
                  int i1 = this.basePos.func_177958_n() - blockpos.func_177958_n();
                  int j1 = this.basePos.func_177952_p() - blockpos.func_177952_p();
                  double d4 = (double)blockpos.func_177956_o() - Math.sqrt((double)(i1 * i1 + j1 * j1)) * this.branchSlope;
                  int k1 = d4 > (double)j ? j : (int)d4;
                  BlockPos blockpos2 = new BlockPos(this.basePos.func_177958_n(), k1, this.basePos.func_177952_p());
                  if (this.checkBlockLine(blockpos2, blockpos) == -1) {
                     this.foliageCoords.add(new FoliageCoordinates(blockpos, blockpos2.func_177956_o()));
                  }
               }
            }
         }
      }

   }

   void crosSection(BlockPos pos, float p_181631_2_, IBlockState p_181631_3_) {
      int i = (int)((double)p_181631_2_ + 0.618);

      for(int j = -i; j <= i; ++j) {
         for(int k = -i; k <= i; ++k) {
            if (Math.pow((double)Math.abs(j) + 0.5, 2.0) + Math.pow((double)Math.abs(k) + 0.5, 2.0) <= (double)(p_181631_2_ * p_181631_2_)) {
               BlockPos blockpos = pos.func_177982_a(j, 0, k);
               IBlockState state = this.world.func_180495_p(blockpos);
               if (state.func_177230_c().isAir(state, this.world, blockpos) || state.func_177230_c().isLeaves(state, this.world, blockpos)) {
                  this.func_175903_a(this.world, blockpos, p_181631_3_);
               }
            }
         }
      }

   }

   float layerSize(int y) {
      if ((float)y < (float)this.heightLimit * 0.3F) {
         return -1.0F;
      } else {
         float f = (float)this.heightLimit / 2.0F;
         float f1 = f - (float)y;
         float f2 = MathHelper.func_76129_c(f * f - f1 * f1);
         if (f1 == 0.0F) {
            f2 = f;
         } else if (Math.abs(f1) >= f) {
            return 0.0F;
         }

         return f2 * 0.5F;
      }
   }

   float leafSize(int y) {
      if (y >= 0 && y < this.leafDistanceLimit) {
         return y != 0 && y != this.leafDistanceLimit - 1 ? 3.0F : 2.0F;
      } else {
         return -1.0F;
      }
   }

   void generateLeafNode(BlockPos pos) {
      for(int i = 0; i < this.leafDistanceLimit; ++i) {
         this.crosSection(pos.func_177981_b(i), this.leafSize(i), Blocks.field_150362_t.func_176223_P().func_177226_a(BlockLeaves.field_176236_b, false));
      }

   }

   void limb(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_) {
      BlockPos blockpos = p_175937_2_.func_177982_a(-p_175937_1_.func_177958_n(), -p_175937_1_.func_177956_o(), -p_175937_1_.func_177952_p());
      int i = this.getGreatestDistance(blockpos);
      float f = (float)blockpos.func_177958_n() / (float)i;
      float f1 = (float)blockpos.func_177956_o() / (float)i;
      float f2 = (float)blockpos.func_177952_p() / (float)i;

      for(int j = 0; j <= i; ++j) {
         BlockPos blockpos1 = p_175937_1_.func_177963_a((double)(0.5F + (float)j * f), (double)(0.5F + (float)j * f1), (double)(0.5F + (float)j * f2));
         BlockLog.EnumAxis blocklog$enumaxis = this.getLogAxis(p_175937_1_, blockpos1);
         this.func_175903_a(this.world, blockpos1, p_175937_3_.func_176223_P().func_177226_a(BlockLog.field_176299_a, blocklog$enumaxis));
      }

   }

   private int getGreatestDistance(BlockPos posIn) {
      int i = MathHelper.func_76130_a(posIn.func_177958_n());
      int j = MathHelper.func_76130_a(posIn.func_177956_o());
      int k = MathHelper.func_76130_a(posIn.func_177952_p());
      if (k > i && k > j) {
         return k;
      } else {
         return j > i ? j : i;
      }
   }

   private BlockLog.EnumAxis getLogAxis(BlockPos p_175938_1_, BlockPos p_175938_2_) {
      BlockLog.EnumAxis blocklog$enumaxis = EnumAxis.Y;
      int i = Math.abs(p_175938_2_.func_177958_n() - p_175938_1_.func_177958_n());
      int j = Math.abs(p_175938_2_.func_177952_p() - p_175938_1_.func_177952_p());
      int k = Math.max(i, j);
      if (k > 0) {
         if (i == k) {
            blocklog$enumaxis = EnumAxis.X;
         } else if (j == k) {
            blocklog$enumaxis = EnumAxis.Z;
         }
      }

      return blocklog$enumaxis;
   }

   void generateLeaves() {
      Iterator var1 = this.foliageCoords.iterator();

      while(var1.hasNext()) {
         FoliageCoordinates worldgenbigtree$foliagecoordinates = (FoliageCoordinates)var1.next();
         this.generateLeafNode(worldgenbigtree$foliagecoordinates);
      }

   }

   boolean leafNodeNeedsBase(int p_76493_1_) {
      return (double)p_76493_1_ >= (double)this.heightLimit * 0.2;
   }

   void generateTrunk() {
      BlockPos blockpos = this.basePos;
      BlockPos blockpos1 = this.basePos.func_177981_b(this.height);
      Block block = Blocks.field_150364_r;
      this.limb(blockpos, blockpos1, block);
      if (this.trunkSize == 2) {
         this.limb(blockpos.func_177974_f(), blockpos1.func_177974_f(), block);
         this.limb(blockpos.func_177974_f().func_177968_d(), blockpos1.func_177974_f().func_177968_d(), block);
         this.limb(blockpos.func_177968_d(), blockpos1.func_177968_d(), block);
      }

   }

   void generateLeafNodeBases() {
      Iterator var1 = this.foliageCoords.iterator();

      while(var1.hasNext()) {
         FoliageCoordinates worldgenbigtree$foliagecoordinates = (FoliageCoordinates)var1.next();
         int i = worldgenbigtree$foliagecoordinates.getBranchBase();
         BlockPos blockpos = new BlockPos(this.basePos.func_177958_n(), i, this.basePos.func_177952_p());
         if (!blockpos.equals(worldgenbigtree$foliagecoordinates) && this.leafNodeNeedsBase(i - this.basePos.func_177956_o())) {
            this.limb(blockpos, worldgenbigtree$foliagecoordinates, Blocks.field_150364_r);
         }
      }

   }

   int checkBlockLine(BlockPos posOne, BlockPos posTwo) {
      BlockPos blockpos = posTwo.func_177982_a(-posOne.func_177958_n(), -posOne.func_177956_o(), -posOne.func_177952_p());
      int i = this.getGreatestDistance(blockpos);
      float f = (float)blockpos.func_177958_n() / (float)i;
      float f1 = (float)blockpos.func_177956_o() / (float)i;
      float f2 = (float)blockpos.func_177952_p() / (float)i;
      if (i == 0) {
         return -1;
      } else {
         for(int j = 0; j <= i; ++j) {
            BlockPos blockpos1 = posOne.func_177963_a((double)(0.5F + (float)j * f), (double)(0.5F + (float)j * f1), (double)(0.5F + (float)j * f2));
            if (!this.isReplaceable(this.world, blockpos1)) {
               return j;
            }
         }

         return -1;
      }
   }

   public void func_175904_e() {
      this.leafDistanceLimit = 5;
   }

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      this.world = worldIn;
      this.basePos = position;
      this.rand = new Random(rand.nextLong());
      if (this.heightLimit == 0) {
         this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
      }

      if (!this.validTreeLocation()) {
         this.world = null;
         return false;
      } else {
         this.generateLeafNodeList();
         this.generateLeaves();
         this.generateTrunk();
         this.generateLeafNodeBases();
         this.world = null;
         return true;
      }
   }

   private boolean validTreeLocation() {
      BlockPos down = this.basePos.func_177977_b();
      IBlockState state = this.world.func_180495_p(down);
      boolean isSoil = state.func_177230_c().canSustainPlant(state, this.world, down, EnumFacing.UP, (BlockSapling)Blocks.field_150345_g);
      if (!isSoil) {
         return false;
      } else {
         int i = this.checkBlockLine(this.basePos, this.basePos.func_177981_b(this.heightLimit - 1));
         if (i == -1) {
            return true;
         } else if (i < 6) {
            return false;
         } else {
            this.heightLimit = i;
            return true;
         }
      }
   }

   static class FoliageCoordinates extends BlockPos {
      private final int branchBase;

      public FoliageCoordinates(BlockPos pos, int p_i45635_2_) {
         super(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         this.branchBase = p_i45635_2_;
      }

      public int getBranchBase() {
         return this.branchBase;
      }
   }
}

package com.pixelmonmod.pixelmon.blocks;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMaxMushroom extends BlockBush {
   protected static final AxisAlignedBB MUSHROOM_AABB = new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 0.6, 0.8);

   public BlockMaxMushroom() {
      this.func_149675_a(true);
      this.func_149711_c(0.0F);
      this.func_149672_a(SoundType.field_185850_c);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return MUSHROOM_AABB;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return PixelmonItems.maxMushroom;
   }

   public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (rand.nextInt(25) == 0) {
         int i = 5;
         int j = true;
         Iterator var7 = BlockPos.func_177975_b(pos.func_177982_a(-4, -1, -4), pos.func_177982_a(4, 1, 4)).iterator();

         while(var7.hasNext()) {
            BlockPos blockpos = (BlockPos)var7.next();
            if (worldIn.func_180495_p(blockpos).func_177230_c() == this) {
               --i;
               if (i <= 0) {
                  return;
               }
            }
         }

         BlockPos blockpos1 = pos.func_177982_a(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

         for(int k = 0; k < 4; ++k) {
            if (worldIn.func_175623_d(blockpos1) && this.func_180671_f(worldIn, blockpos1, this.func_176223_P())) {
               pos = blockpos1;
            }

            blockpos1 = pos.func_177982_a(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
         }

         if (worldIn.func_175623_d(blockpos1) && this.func_180671_f(worldIn, blockpos1, this.func_176223_P())) {
            worldIn.func_180501_a(blockpos1, this.func_176223_P(), 2);
         }
      }

   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return super.func_176196_c(worldIn, pos) && this.func_180671_f(worldIn, pos, this.func_176223_P());
   }

   protected boolean func_185514_i(IBlockState state) {
      return state.func_185913_b();
   }

   public boolean func_180671_f(World worldIn, BlockPos pos, IBlockState state) {
      if (pos.func_177956_o() >= 0 && pos.func_177956_o() < 256) {
         IBlockState iblockstate = worldIn.func_180495_p(pos.func_177977_b());
         return iblockstate.func_177230_c() == Blocks.field_150391_bh;
      } else {
         return false;
      }
   }
}

package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSpheroid extends WorldGenerator {
   private static final IBlockState OBSIDIAN;
   private static final IBlockState DIAMOND_BLOCK;

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      float f = (float)(rand.nextInt(3) + 4);

      for(int i = 0; f > 0.5F; --i) {
         for(int j = MathHelper.func_76141_d(-f); j <= MathHelper.func_76123_f(f); ++j) {
            for(int k = MathHelper.func_76141_d(-f); k <= MathHelper.func_76123_f(f); ++k) {
               if ((float)(j * j + k * k) <= (f + 1.0F) * (f + 1.0F)) {
                  this.func_175903_a(worldIn, position.func_177982_a(j, i, k), OBSIDIAN);
                  this.func_175903_a(worldIn, position.func_177982_a(j, -i + 1, k), OBSIDIAN);
               }
            }
         }

         f = (float)((double)f - ((double)rand.nextInt(2) + 0.5));
      }

      return true;
   }

   static {
      OBSIDIAN = PixelmonBlocks.deepSea.func_176223_P();
      DIAMOND_BLOCK = Blocks.field_150484_ah.func_176223_P();
   }
}

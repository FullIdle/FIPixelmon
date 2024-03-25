package com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.generators;

import java.util.Random;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockPrismarine.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenOceanRuinA extends WorldGenerator {
   private static final IBlockState PRISMARINE;
   private static final IBlockState PRISMARINE_BRICKS;
   private static final IBlockState PRISMARINE_DARK;
   private static final IBlockState SEA_LANTERN;
   private static final IBlockState GOLD_BLOCK;

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      double ruinedFactor = MathHelper.func_151237_a(rand.nextDouble() + 0.25, 0.4, 0.9);
      int facing = rand.nextInt(4);

      for(int x = 0; x < 5; ++x) {
         for(int y = 0; y < 5; ++y) {
            for(int z = 0; z < 5; ++z) {
               BlockPos pos = position.func_177982_a(x, y, z);
               if ((y <= 0 || y >= 4 || x <= 0 || x >= 4 || z <= 0 || z >= 4) && (rand.nextDouble() < ruinedFactor || y == 4 && x == 2 && z == 2)) {
                  if (y == 0) {
                     this.func_175903_a(worldIn, pos, PRISMARINE_BRICKS);
                  } else if (y == 4) {
                     if (x != 0 && x != 4 && z != 0 && z != 4) {
                        if (x == 2 && z == 2) {
                           this.func_175903_a(worldIn, pos, SEA_LANTERN);
                        } else {
                           this.func_175903_a(worldIn, pos, PRISMARINE_DARK);
                        }
                     }
                  } else if (x == 0 && z == 0 || x == 0 && z == 4 || x == 4 && z == 0 || x == 4 && z == 4) {
                     this.func_175903_a(worldIn, pos, PRISMARINE_DARK);
                  } else if (y == 3 || facing == 0 && (x != 2 || z != 0) || facing == 1 && (x != 0 || z != 2) || facing == 2 && (x != 2 || z != 4) || facing == 3 && (x != 4 || z != 2)) {
                     this.func_175903_a(worldIn, pos, PRISMARINE);
                  }
               }

               IBlockState state = worldIn.func_180495_p(pos);
               worldIn.func_184138_a(pos, state, state, 3);
               worldIn.func_175726_f(pos).func_76594_o();
            }
         }
      }

      worldIn.func_175704_b(position.func_177982_a(-5, -5, -5), position.func_177982_a(9, 9, 9));
      worldIn.func_180501_a(position.func_177982_a(2, 4, 2), Blocks.field_180398_cJ.func_176223_P(), 2);
      return true;
   }

   static {
      PRISMARINE = Blocks.field_180397_cI.func_176223_P().func_177226_a(BlockPrismarine.field_176332_a, EnumType.ROUGH);
      PRISMARINE_BRICKS = Blocks.field_180397_cI.func_176223_P().func_177226_a(BlockPrismarine.field_176332_a, EnumType.BRICKS);
      PRISMARINE_DARK = Blocks.field_180397_cI.func_176223_P().func_177226_a(BlockPrismarine.field_176332_a, EnumType.DARK);
      SEA_LANTERN = Blocks.field_180398_cJ.func_176223_P();
      GOLD_BLOCK = Blocks.field_150340_R.func_176223_P();
   }
}

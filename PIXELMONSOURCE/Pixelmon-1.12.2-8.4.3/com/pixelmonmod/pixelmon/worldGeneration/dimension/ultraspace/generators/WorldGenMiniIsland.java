package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMiniIsland extends WorldGenerator {
   private static final IBlockState GRASS;
   private static final IBlockState DIRT;
   private static final IBlockState MYCELIUM;
   private static final IBlockState STONE;
   private static final IBlockState SANDSTONE;
   private static final IBlockState NETHERRACK;
   private static final IBlockState END_STONE;
   private static final IBlockState COAL_ORE;
   private static final IBlockState IRON_ORE;
   private static final IBlockState BAUXITE;
   private static final IBlockState GOLD_ORE;
   private static final IBlockState EMERALD_ORE;
   private static final IBlockState LAPIS_ORE;
   private static final IBlockState REDSTONE_ORE;
   private static final IBlockState DIAMOND_ORE;
   private static final IBlockState QUARTZ_ORE;

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      float f = (float)(rand.nextInt(3) + 4);
      IBlockState toGenerate;
      switch (rand.nextInt(7)) {
         case 0:
            toGenerate = MYCELIUM;
            break;
         case 1:
            toGenerate = DIRT;
            break;
         case 2:
            toGenerate = SANDSTONE;
            break;
         case 3:
            toGenerate = END_STONE;
            break;
         case 4:
            toGenerate = GRASS;
            break;
         case 5:
            toGenerate = STONE;
            break;
         case 6:
            toGenerate = NETHERRACK;
            break;
         default:
            toGenerate = STONE;
      }

      for(int i = 0; f > 0.5F; --i) {
         for(int j = MathHelper.func_76141_d(-f); j <= MathHelper.func_76123_f(f); ++j) {
            for(int k = MathHelper.func_76141_d(-f); k <= MathHelper.func_76123_f(f); ++k) {
               if ((float)(j * j + k * k) <= (f + 1.0F) * (f + 1.0F)) {
                  double oreGen;
                  if (toGenerate.func_177230_c() == STONE.func_177230_c()) {
                     oreGen = rand.nextDouble();
                     if (oreGen < 0.003) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), DIAMOND_ORE);
                     } else if (oreGen < 0.007) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), EMERALD_ORE);
                     } else if (oreGen < 0.012) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), REDSTONE_ORE);
                     } else if (oreGen < 0.015) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), LAPIS_ORE);
                     } else if (oreGen < 0.03) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), GOLD_ORE);
                     } else if (oreGen < 0.045) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), BAUXITE);
                     } else if (oreGen < 0.06) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), IRON_ORE);
                     } else if (oreGen < 0.1) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), COAL_ORE);
                     } else {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), toGenerate);
                     }
                  } else if (toGenerate.func_177230_c() == NETHERRACK.func_177230_c()) {
                     oreGen = rand.nextDouble();
                     if (oreGen < 0.07) {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), QUARTZ_ORE);
                     } else {
                        this.func_175903_a(worldIn, position.func_177982_a(j, i, k), toGenerate);
                     }
                  } else {
                     this.func_175903_a(worldIn, position.func_177982_a(j, i, k), toGenerate);
                  }
               }
            }
         }

         f = (float)((double)f - ((double)rand.nextInt(2) + 0.5));
      }

      return true;
   }

   static {
      GRASS = Blocks.field_150349_c.func_176223_P();
      DIRT = Blocks.field_150346_d.func_176223_P();
      MYCELIUM = Blocks.field_150391_bh.func_176223_P();
      STONE = Blocks.field_150348_b.func_176223_P();
      SANDSTONE = Blocks.field_150322_A.func_176223_P();
      NETHERRACK = Blocks.field_150424_aL.func_176223_P();
      END_STONE = Blocks.field_150377_bs.func_176223_P();
      COAL_ORE = Blocks.field_150365_q.func_176223_P();
      IRON_ORE = Blocks.field_150366_p.func_176223_P();
      BAUXITE = PixelmonBlocks.bauxite.func_176223_P();
      GOLD_ORE = Blocks.field_150352_o.func_176223_P();
      EMERALD_ORE = Blocks.field_150412_bA.func_176223_P();
      LAPIS_ORE = Blocks.field_150369_x.func_176223_P();
      REDSTONE_ORE = Blocks.field_150450_ax.func_176223_P();
      DIAMOND_ORE = Blocks.field_150482_ag.func_176223_P();
      QUARTZ_ORE = Blocks.field_150449_bY.func_176223_P();
   }
}

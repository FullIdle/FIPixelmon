package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.biomes;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenForestTreeA;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenForestTreeB;
import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UltraForest extends Biome {
   protected static final WorldGenForestTreeA FLAT_TREE = new WorldGenForestTreeA();
   protected static final WorldGenForestTreeB BIG_TREE = new WorldGenForestTreeB();
   protected static final IBlockState WATER_LILY;

   public UltraForest(Biome.BiomeProperties properties) {
      super(properties);
      this.field_76760_I.field_76832_z = 20;
      this.field_76760_I.field_76799_E = 6;
      this.field_76760_I.field_76803_B = 10;
      this.field_76760_I.field_76805_H = 5;
      this.field_76760_I.field_76801_G = 0;
      this.field_76760_I.field_76833_y = 8;
      this.field_76760_I.field_76806_I = 2;
   }

   public BlockFlower.EnumFlowerType func_180623_a(Random rand, BlockPos pos) {
      return super.func_180623_a(rand, pos);
   }

   public void func_180622_a(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
      if (rand.nextDouble() < 0.05) {
         int i = x & 15;
         int j = z & 15;

         for(int k = 255; k >= 0; --k) {
            if (chunkPrimerIn.func_177856_a(j, k, i).func_185904_a() != Material.field_151579_a) {
               if (chunkPrimerIn.func_177856_a(j, k, i).func_185904_a() != Material.field_151586_h) {
                  chunkPrimerIn.func_177855_a(j, k, i, field_185372_h);
                  worldIn.func_175654_a(new BlockPos(j, k, i), field_185372_h.func_177230_c(), 0, 1);
                  if (rand.nextDouble() < 0.005) {
                     chunkPrimerIn.func_177855_a(j, k + 1, i, WATER_LILY);
                  }
               }
               break;
            }
         }
      }

      this.func_180628_b(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
   }

   public void func_180624_a(World worldIn, Random rand, BlockPos pos) {
      super.func_180624_a(worldIn, rand, pos);
   }

   public WorldGenAbstractTree func_150567_a(Random rand) {
      return (WorldGenAbstractTree)(rand.nextDouble() < 0.4 ? BIG_TREE : FLAT_TREE);
   }

   public Class func_150562_l() {
      return UltraForest.class;
   }

   @SideOnly(Side.CLIENT)
   public int func_180627_b(BlockPos pos) {
      return super.func_180627_b(pos);
   }

   static {
      WATER_LILY = Blocks.field_150392_bi.func_176223_P();
   }
}

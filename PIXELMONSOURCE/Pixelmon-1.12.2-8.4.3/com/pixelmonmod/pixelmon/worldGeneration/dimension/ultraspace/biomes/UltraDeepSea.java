package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.biomes;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenCoalTree;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenSpike;
import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UltraDeepSea extends Biome {
   private static final WorldGenCoalTree COAL_TREE = new WorldGenCoalTree(false);
   private static final WorldGenSpike SPIKE;

   public UltraDeepSea(Biome.BiomeProperties properties) {
      super(properties);
      this.field_76760_I.field_76832_z = 5;
      this.field_76760_I.field_76805_H = 0;
      this.field_76760_I.field_76801_G = 0;
      this.field_76752_A = PixelmonBlocks.deepSea.func_176223_P();
      this.field_76753_B = PixelmonBlocks.deepSea.func_176223_P();
   }

   public BlockFlower.EnumFlowerType func_180623_a(Random rand, BlockPos pos) {
      return super.func_180623_a(rand, pos);
   }

   public void func_180622_a(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
      this.func_180628_b(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
   }

   public void func_180624_a(World worldIn, Random rand, BlockPos pos) {
      super.func_180624_a(worldIn, rand, pos);
   }

   public WorldGenAbstractTree func_150567_a(Random rand) {
      int seed = rand.nextInt(10);
      return (WorldGenAbstractTree)(seed < 2 ? COAL_TREE : SPIKE);
   }

   public Class func_150562_l() {
      return UltraDeepSea.class;
   }

   @SideOnly(Side.CLIENT)
   public int func_180627_b(BlockPos pos) {
      return super.func_180627_b(pos);
   }

   static {
      SPIKE = new WorldGenSpike(PixelmonBlocks.deepSea.func_176223_P(), Blocks.field_150368_y.func_176223_P(), Blocks.field_150484_ah.func_176223_P());
   }
}

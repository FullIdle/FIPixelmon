package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.biomes;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators.WorldGenForestTreeB;
import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UltraJungle extends Biome {
   private static final IBlockState JUNGLE_LOG;
   private static final IBlockState JUNGLE_LEAF;
   private static final IBlockState OAK_LEAF;
   protected static final WorldGenForestTreeB BIG_TREE;

   public UltraJungle(Biome.BiomeProperties properties) {
      super(properties);
      this.field_76760_I.field_76832_z = 60;
      this.field_76760_I.field_76803_B = 25;
      this.field_76760_I.field_76802_A = 20;
      this.field_76760_I.field_76833_y = 8;
      this.field_76760_I.field_76806_I = 2;
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
      if (rand.nextInt(14) == 0) {
         return BIG_TREE;
      } else if (rand.nextInt(2) == 0) {
         return new WorldGenShrub(JUNGLE_LOG, OAK_LEAF);
      } else {
         return (WorldGenAbstractTree)(rand.nextInt(2) == 0 ? new WorldGenMegaJungle(false, 10, 20, JUNGLE_LOG, JUNGLE_LEAF) : new WorldGenTrees(false, 4 + rand.nextInt(7), JUNGLE_LOG, JUNGLE_LEAF, true));
      }
   }

   public Class func_150562_l() {
      return UltraJungle.class;
   }

   @SideOnly(Side.CLIENT)
   public int func_180627_b(BlockPos pos) {
      return super.func_180627_b(pos);
   }

   static {
      JUNGLE_LOG = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, EnumType.JUNGLE);
      JUNGLE_LEAF = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, EnumType.JUNGLE).func_177226_a(BlockLeaves.field_176236_b, false);
      OAK_LEAF = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, EnumType.OAK).func_177226_a(BlockLeaves.field_176236_b, false);
      BIG_TREE = new WorldGenForestTreeB();
   }
}

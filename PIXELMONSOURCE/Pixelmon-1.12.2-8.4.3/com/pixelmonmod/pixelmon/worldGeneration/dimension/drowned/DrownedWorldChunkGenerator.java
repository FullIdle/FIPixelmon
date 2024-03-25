package com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.generators.WorldGenOceanRuinA;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.generators.WorldGenOceanRuinB;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.ForgeEventFactory;

public class DrownedWorldChunkGenerator implements IChunkGenerator {
   protected static final IBlockState WATER;
   protected static final IBlockState BARRIER;
   private final WorldGenOceanRuinA OCEAN_RUIN_A = new WorldGenOceanRuinA();
   private final WorldGenOceanRuinB OCEAN_RUIN_B = new WorldGenOceanRuinB();
   private final Random rand;
   private final World world;
   private final WorldType terrainType;
   private final double[] heightMap;
   private Biome[] biomesForGeneration;
   private double[] depthBuffer;

   public DrownedWorldChunkGenerator(World worldIn, long seed) {
      this.world = worldIn;
      this.terrainType = DrownedWorld.WORLD_TYPE;
      this.rand = new Random(seed);
      this.heightMap = new double[825];
   }

   public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
      DrownedWorldBiomeProvider bp = (DrownedWorldBiomeProvider)this.world.func_72959_q();
      this.biomesForGeneration = bp.func_76937_a(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);

      for(int cx = 0; cx < 16; ++cx) {
         for(int cz = 0; cz < 16; ++cz) {
            for(int cy = 0; cy < 256; ++cy) {
               primer.func_177855_a(cx, cy, cz, cy != 0 && cy != 255 ? WATER : BARRIER);
            }
         }
      }

   }

   public Chunk func_185932_a(int x, int z) {
      ChunkPrimer chunkprimer = new ChunkPrimer();
      DrownedWorldBiomeProvider bp = (DrownedWorldBiomeProvider)this.world.func_72959_q();
      this.setBlocksInChunk(x, z, chunkprimer);
      this.biomesForGeneration = bp.func_76933_b(this.biomesForGeneration, x * 16, z * 16, 16, 16);
      this.removeBedrockLayer(chunkprimer);
      Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
      byte[] abyte = chunk.func_76605_m();

      for(int i = 0; i < abyte.length; ++i) {
         byte biomeId = (byte)Biome.func_185362_a(this.biomesForGeneration[i]);
         abyte[i] = biomeId;
      }

      chunk.func_76603_b();
      chunk.func_76630_e();
      return chunk;
   }

   public void func_185931_b(int x, int z) {
      BlockFalling.field_149832_M = false;
      int i = x * 16;
      int j = z * 16;
      BlockPos blockpos = new BlockPos(i, 0, j);
      this.rand.setSeed(this.world.func_72905_C());
      long k = this.rand.nextLong() / 2L * 2L + 1L;
      long l = this.rand.nextLong() / 2L * 2L + 1L;
      this.rand.setSeed((long)x * k + (long)z * l ^ this.world.func_72905_C());
      boolean flag = false;
      new ChunkPos(x, z);
      ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);
      if (this.rand.nextInt(15) == 0) {
         for(int count = 0; count < this.rand.nextInt(7); ++count) {
            this.OCEAN_RUIN_A.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 20 + this.rand.nextInt(216), this.rand.nextInt(16) + 8));
         }
      } else if (this.rand.nextInt(35) == 0) {
         this.OCEAN_RUIN_B.func_180709_b(this.world, this.rand, blockpos.func_177982_a(this.rand.nextInt(16) + 8, 20 + this.rand.nextInt(216), this.rand.nextInt(16) + 8));
      }

      BlockFalling.field_149832_M = false;
   }

   public boolean func_185933_a(Chunk chunkIn, int x, int z) {
      return false;
   }

   public List func_177458_a(EnumCreatureType creatureType, BlockPos pos) {
      Biome biome = this.world.func_180494_b(pos);
      return biome.func_76747_a(creatureType);
   }

   @Nullable
   public BlockPos func_180513_a(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
      return null;
   }

   public void func_180514_a(Chunk chunkIn, int x, int z) {
   }

   public boolean func_193414_a(World worldIn, String structureName, BlockPos pos) {
      return false;
   }

   public void removeBedrockLayer(ChunkPrimer primer) {
      for(int a = 0; a < 16; ++a) {
         for(int b = 0; b < 6; ++b) {
            for(int c = 0; c < 16; ++c) {
               if (primer.func_177856_a(a, b, c).func_177230_c() == Blocks.field_150357_h) {
                  primer.func_177855_a(a, b, c, Blocks.field_150355_j.func_176223_P());
               }
            }
         }
      }

   }

   static {
      WATER = Blocks.field_150355_j.func_176223_P();
      BARRIER = Blocks.field_180401_cv.func_176223_P();
   }
}

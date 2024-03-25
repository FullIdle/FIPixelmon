package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpaceChunkGenerator;
import java.util.Random;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureEndCityPieces;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenEndCityUltraSpace extends MapGenStructure {
   private final int citySpacing = 20;
   private final int minCitySeparation = 11;
   private final UltraSpaceChunkGenerator provider;

   public MapGenEndCityUltraSpace(UltraSpaceChunkGenerator p_i46665_1_) {
      this.provider = p_i46665_1_;
   }

   public String func_143025_a() {
      return "EndCity";
   }

   protected boolean func_75047_a(int chunkX, int chunkZ) {
      int i = chunkX;
      int j = chunkZ;
      if (chunkX < 0) {
         chunkX -= 19;
      }

      if (chunkZ < 0) {
         chunkZ -= 19;
      }

      int k = chunkX / 20;
      int l = chunkZ / 20;
      Random random = this.field_75039_c.func_72843_D(k, l, 10387313);
      k *= 20;
      l *= 20;
      k += (random.nextInt(9) + random.nextInt(9)) / 2;
      l += (random.nextInt(9) + random.nextInt(9)) / 2;
      if (i == k && j == l && this.provider.isIslandChunk(i, j)) {
         int i1 = getYPosForStructure(i, j, this.provider);
         return i1 >= 60;
      } else {
         return false;
      }
   }

   protected StructureStart func_75049_b(int chunkX, int chunkZ) {
      return new Start(this.field_75039_c, this.provider, this.field_75038_b, chunkX, chunkZ);
   }

   public BlockPos func_180706_b(World worldIn, BlockPos pos, boolean findUnexplored) {
      this.field_75039_c = worldIn;
      return func_191069_a(worldIn, this, pos, 20, 11, 10387313, true, 100, findUnexplored);
   }

   private static int getYPosForStructure(int p_191070_0_, int p_191070_1_, UltraSpaceChunkGenerator p_191070_2_) {
      Random random = new Random((long)(p_191070_0_ + p_191070_1_ * 10387313));
      Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
      ChunkPrimer chunkprimer = new ChunkPrimer();
      p_191070_2_.setBlocksInChunk(p_191070_0_, p_191070_1_, chunkprimer);
      int i = 5;
      int j = 5;
      if (rotation == Rotation.CLOCKWISE_90) {
         i = -5;
      } else if (rotation == Rotation.CLOCKWISE_180) {
         i = -5;
         j = -5;
      } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
         j = -5;
      }

      int k = chunkprimer.func_186138_a(7, 7);
      int l = chunkprimer.func_186138_a(7, 7 + j);
      int i1 = chunkprimer.func_186138_a(7 + i, 7);
      int j1 = chunkprimer.func_186138_a(7 + i, 7 + j);
      int k1 = Math.min(Math.min(k, l), Math.min(i1, j1));
      return k1;
   }

   public static class Start extends StructureStart {
      private boolean isSizeable;

      public Start() {
      }

      public Start(World worldIn, UltraSpaceChunkGenerator chunkProvider, Random random, int chunkX, int chunkZ) {
         super(chunkX, chunkZ);
         this.create(worldIn, chunkProvider, random, chunkX, chunkZ);
      }

      private void create(World worldIn, UltraSpaceChunkGenerator chunkProvider, Random rnd, int chunkX, int chunkZ) {
         Random random = new Random((long)(chunkX + chunkZ * 10387313));
         Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
         int i = MapGenEndCityUltraSpace.getYPosForStructure(chunkX, chunkZ, chunkProvider);
         if (i < 60) {
            this.isSizeable = false;
         } else {
            BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
            StructureEndCityPieces.func_191087_a(worldIn.func_72860_G().func_186340_h(), blockpos, rotation, this.field_75075_a, rnd);
            this.func_75072_c();
            this.isSizeable = true;
         }

      }

      public boolean func_75069_d() {
         return this.isSizeable;
      }
   }
}

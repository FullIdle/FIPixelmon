package com.pixelmonmod.pixelmon.api.world;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;

public class BlockCollection {
   public final Entity cause;
   public final World world;
   public final int minX;
   public final int maxX;
   public final int minY;
   public final int maxY;
   public final int minZ;
   public final int maxZ;
   private IBlockState[][][] blockData;
   private byte[][][] lightData;
   private Biome[][] biomeData;

   public BlockCollection(Entity cause, World world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
      this.cause = cause;
      this.world = world;
      this.minX = minX;
      this.maxX = maxX;
      this.minY = MathHelper.func_76125_a(minY, 0, 255);
      this.maxY = MathHelper.func_76125_a(maxY, 0, 255);
      this.minZ = minZ;
      this.maxZ = maxZ;
      this.blockData = new IBlockState[maxX - minX + 1][256][maxZ - minZ + 1];
      this.lightData = new byte[maxX - minX + 1][256][maxZ - minZ + 1];
      this.biomeData = new Biome[maxX - minX + 1][maxZ - minZ + 1];
      BiomeProvider provider = world.func_72959_q();
      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(minX, minY, minZ);
      BlockPos.MutableBlockPos lightPos = new BlockPos.MutableBlockPos(minX, minY, minZ);
      Chunk chunk = null;

      for(int x = 0; x < maxX - minX + 1; ++x) {
         for(int z = 0; z < maxZ - minZ + 1; ++z) {
            if ((chunk == null || x + minX >> 4 != chunk.field_76635_g || z + minZ >> 4 != chunk.field_76647_h) && (chunk = world.func_72863_F().func_186026_b(x + minX >> 4, z + minZ >> 4)) == null) {
               this.biomeData[x][z] = Biomes.field_185440_P;
            } else {
               this.biomeData[x][z] = chunk.func_177411_a(pos.func_181079_c(x + minX, 255, z + minZ), provider);

               for(int y = 0; y < 256; ++y) {
                  IBlockState state = chunk.func_186032_a(x + minX, y, z + minZ);
                  this.blockData[x][y][z] = state;
                  this.lightData[x][y][z] = state.func_185914_p() ? 0 : (byte)chunk.func_177443_a(lightPos.func_181079_c(x + minX, y, z + minZ), 0);
               }
            }
         }
      }

   }

   public Block getBlock(int x, int y, int z) {
      return this.blockData[x - this.minX][y][z - this.minZ] == null ? Blocks.field_150348_b : this.blockData[x - this.minX][y][z - this.minZ].func_177230_c();
   }

   public int getLight(int x, int y, int z) {
      return this.lightData[x - this.minX][y][z - this.minZ];
   }

   @Nullable
   public IBlockState getBlockState(int x, int y, int z) {
      return this.blockData[x - this.minX][y][z - this.minZ];
   }

   public Biome getBiome(int x, int z) {
      return this.biomeData[x - this.minX][z - this.minZ];
   }
}

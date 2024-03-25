package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenLeafStoneOre implements IWorldGenerator {
   public boolean generate(World world, Random rand, int x, int y, int z) {
      BlockPos pos = new BlockPos(x, y, z);
      world.func_180501_a(pos, PixelmonBlocks.leafStoneOre.func_176223_P(), 18);
      return true;
   }

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (world.field_73011_w.func_76569_d()) {
         Biome biome = world.func_180494_b(new BlockPos(chunkX * 16, 0, chunkZ * 16));
         if (BiomeDictionary.getTypes(biome).contains(Type.FOREST)) {
            for(int i = 0; i < random.nextInt(3) + 1; ++i) {
               int x = random.nextInt(16) + chunkX * 16;
               int z = random.nextInt(16) + chunkZ * 16;
               int y = world.func_175645_m(new BlockPos(x, 0, z)).func_177956_o();
               IBlockState block = world.func_180495_p(new BlockPos(x, y - 1, z));
               Material blockMaterial = block.func_185904_a();
               if (blockMaterial == Material.field_151584_j) {
                  while(blockMaterial != Material.field_151576_e && y > 0) {
                     --y;
                     block = world.func_180495_p(new BlockPos(x, y - 1, z));
                     blockMaterial = block.func_185904_a();
                  }

                  if (y > 0) {
                     this.generate(world, random, x, y - 1, z);
                  }
               }
            }
         }

      }
   }
}

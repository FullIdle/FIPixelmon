package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenDawnDuskOre implements IWorldGenerator {
   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      boolean hasPlains = false;
      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

      int i;
      for(i = 0; i < 16; ++i) {
         pos.func_181079_c((chunkX << 4) + i, 80, (chunkZ << 4) + i);
         Biome biome = world.func_180494_b(pos);
         hasPlains = BiomeDictionary.getTypes(biome).contains(Type.PLAINS);
         if (hasPlains) {
            break;
         }
      }

      if (hasPlains) {
         for(i = 0; i < 20; ++i) {
            int xPos = (chunkX << 4) + random.nextInt(15);
            int zPos = (chunkZ << 4) + random.nextInt(15);
            int yPos = world.func_175672_r(pos.func_181079_c(xPos, 0, zPos)).func_177956_o() - random.nextInt(10);
            pos.func_181079_c(xPos, yPos, zPos);
            if (world.func_180495_p(pos).func_177230_c() == Blocks.field_150348_b && (double)random.nextFloat() <= 0.04) {
               world.func_180501_a(pos, PixelmonBlocks.dawnduskStoneOre.func_176223_P(), 18);
            }
         }
      }

   }
}

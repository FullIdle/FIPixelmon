package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenThunderStoneOre implements IWorldGenerator {
   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      for(int i = 0; i < 30; ++i) {
         int xPos = random.nextInt(16) + chunkX * 16;
         int zPos = random.nextInt(16) + chunkZ * 16;
         int yPos = random.nextInt(50) + 75;
         (new WorldGenMinable(PixelmonBlocks.thunderStoneOre.func_176223_P(), 2 + random.nextInt(2))).func_180709_b(world, random, new BlockPos(xPos, yPos, zPos));
      }

   }
}

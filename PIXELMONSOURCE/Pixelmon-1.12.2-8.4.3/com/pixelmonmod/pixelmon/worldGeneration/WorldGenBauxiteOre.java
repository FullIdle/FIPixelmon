package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenBauxiteOre implements IWorldGenerator {
   WorldGenMinable bauxiteGen;

   public WorldGenBauxiteOre() {
      this.bauxiteGen = new WorldGenMinable(PixelmonBlocks.bauxite.func_176223_P(), 8);
   }

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      for(int i = 0; i < 5; ++i) {
         int xPos = random.nextInt(2) + chunkX * 16;
         int zPos = random.nextInt(4) + chunkZ * 16;
         int yPos = random.nextInt(30) + 30;
         this.bauxiteGen.func_180709_b(world, random, new BlockPos(xPos, yPos, zPos));
      }

   }
}

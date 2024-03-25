package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GenericOreGenerator implements IWorldGenerator {
   private Block blockToGenerate;

   public GenericOreGenerator(Block blockToGenerate) {
      this.blockToGenerate = blockToGenerate;
   }

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (world.field_73011_w.func_76569_d()) {
         for(int i = 0; i < 4; ++i) {
            BlockPos pos = new BlockPos(random.nextInt(16) + chunkX * 16, RandomHelper.rand.nextInt(64), random.nextInt(16) + chunkZ * 16);
            WorldGenMinable worldGenMinable = new WorldGenMinable(this.blockToGenerate.func_176223_P(), 5);

            try {
               worldGenMinable.func_180709_b(world, random, pos);
            } catch (NullPointerException var11) {
               Pixelmon.LOGGER.error(var11);
            }
         }

      }
   }
}

package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.worldGeneration.structure.GeneratorHelper;
import java.util.Random;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenMaxMushrooms implements IWorldGenerator {
   public void generate(Random r, int chunkX, int chunkZ, World world, IChunkGenerator chunkGen, IChunkProvider chunkProvider) {
      if (world.field_73011_w.func_76569_d() || GeneratorHelper.isUltraSpace(world)) {
         Biome biome = world.func_180494_b(new BlockPos(chunkX * 16, 0, chunkZ * 16));
         if ((biome == Biomes.field_76789_p || biome == Biomes.field_76788_q) && r.nextInt(25) == 0) {
            int x = r.nextInt(16) + chunkX * 16;
            int z = r.nextInt(16) + chunkZ * 16;
            int y = world.func_175645_m(new BlockPos(x, 0, z)).func_177956_o();
            if (world.func_180495_p(new BlockPos(x, y - 1, z)).func_177230_c() == Blocks.field_150391_bh) {
               world.func_180501_a(new BlockPos(x, y, z), PixelmonBlocks.maxMushroomsBlock.func_176223_P(), 18);
            }
         }

      }
   }
}

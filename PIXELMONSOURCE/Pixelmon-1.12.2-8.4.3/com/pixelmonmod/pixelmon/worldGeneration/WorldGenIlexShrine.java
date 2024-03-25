package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.enums.EnumUsed;
import com.pixelmonmod.pixelmon.blocks.machines.BlockIlexShrine;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.worldGeneration.structure.GeneratorHelper;
import java.util.Random;
import java.util.Set;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenIlexShrine implements IWorldGenerator {
   public void generate(Random r, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (PixelmonConfig.spawnCelebiShrines) {
         if (world.field_73011_w.func_76569_d() || GeneratorHelper.isUltraSpace(world)) {
            Biome biome = world.func_180494_b(new BlockPos(chunkX * 16, 0, chunkZ * 16));
            if (r.nextDouble() < PixelmonConfig.ilexShrineSpawnRate && ((Set)BetterSpawnerConfig.INSTANCE.cachedBiomeCategories.get("forests")).contains(biome)) {
               int x = r.nextInt(16) + chunkX * 16;
               int z = r.nextInt(16) + chunkZ * 16;
               int y = world.func_175645_m(new BlockPos(x, 0, z)).func_177956_o();
               if (world.func_180495_p(new BlockPos(x, y - 1, z)).func_177230_c() == Blocks.field_150349_c) {
                  EnumFacing facing = EnumFacing.field_176754_o[r.nextInt(4)];
                  world.func_175656_a(new BlockPos(x, y, z), PixelmonBlocks.shrineIlex.func_176223_P().func_177226_a(BlockIlexShrine.BLOCKPOS, EnumBlockPos.BOTTOM).func_177226_a(BlockProperties.FACING, facing).func_177226_a(BlockIlexShrine.USED, EnumUsed.NO));
                  world.func_175656_a(new BlockPos(x, y + 1, z), PixelmonBlocks.shrineIlex.func_176223_P().func_177226_a(BlockIlexShrine.BLOCKPOS, EnumBlockPos.TOP).func_177226_a(BlockProperties.FACING, facing).func_177226_a(BlockIlexShrine.USED, EnumUsed.NO));
               }
            }

         }
      }
   }
}

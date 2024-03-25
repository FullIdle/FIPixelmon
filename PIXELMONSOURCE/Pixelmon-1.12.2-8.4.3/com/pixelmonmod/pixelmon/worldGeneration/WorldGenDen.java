package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenDen implements IWorldGenerator {
   private final ArrayList blocks;

   public WorldGenDen() {
      this.blocks = new ArrayList(Arrays.asList(Blocks.field_150349_c, Blocks.field_150346_d, Blocks.field_150354_m, Blocks.field_150348_b, Blocks.field_150351_n, Blocks.field_150433_aE, Blocks.field_150431_aC, Blocks.field_150435_aG, Blocks.field_150355_j, Blocks.field_150377_bs, Blocks.field_150424_aL, Blocks.field_150425_aM, Blocks.field_150391_bh, PixelmonBlocks.deepSea, Blocks.field_150353_l, Blocks.field_150406_ce, Blocks.field_150405_ch, Blocks.field_150322_A, Blocks.field_180395_cM));
   }

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (PixelmonConfig.usesGen8Features(world.field_73011_w.getDimension())) {
         if (world instanceof WorldServer && random.nextFloat() < PixelmonConfig.denSpawnChancePerChunk) {
            int xPos = random.nextInt(16) + chunkX * 16;
            int zPos = random.nextInt(16) + chunkZ * 16;
            int yPos;
            if (!(chunkGenerator instanceof ChunkGeneratorHell)) {
               yPos = world.func_175645_m(new BlockPos(xPos, 0, zPos)).func_177956_o() - 1;
            } else {
               int start = random.nextInt(50) + 10;
               if (world.func_175623_d(new BlockPos(xPos, start, zPos))) {
                  return;
               }

               int scan = start;

               while(!world.func_175623_d(new BlockPos(xPos, scan, zPos))) {
                  ++scan;
                  if (scan >= 250) {
                     return;
                  }
               }

               yPos = scan - 1;
            }

            Block block = world.func_180495_p(new BlockPos(xPos, yPos - 1, zPos)).func_177230_c();
            if (this.blocks.contains(block) || BetterSpawnerConfig.getLandBlocks().contains(block)) {
               boolean canSpawn = true;
               boolean liquid = block instanceof BlockLiquid;

               int ix;
               int iz;
               for(ix = -1; ix < 2; ++ix) {
                  for(iz = -1; iz < 2; ++iz) {
                     Block block2 = world.func_180495_p(new BlockPos(xPos + ix, yPos, zPos + iz)).func_177230_c();
                     if (block2 instanceof BlockLiquid) {
                        liquid = true;
                     }

                     if (!this.blocks.contains(block2) && !BetterSpawnerConfig.getLandBlocks().contains(block2)) {
                        canSpawn = false;
                     }
                  }
               }

               if (canSpawn && (!liquid || random.nextFloat() < PixelmonConfig.denAdditionalSpawnChanceOnLiquid)) {
                  for(ix = -2; ix < 3; ++ix) {
                     for(iz = -2; iz < 3; ++iz) {
                        for(int iy = 0; iy < 3; ++iy) {
                           world.func_175698_g(new BlockPos(xPos + ix, yPos + 1 + iy, zPos + iz));
                        }
                     }
                  }

                  FMLCommonHandler.instance().getMinecraftServerInstance().func_152344_a(() -> {
                     world.func_72838_d(new EntityDen(world, (double)xPos + 0.5, (double)(yPos + 1), (double)zPos + 0.5));
                  });
               }
            }
         }

      }
   }
}

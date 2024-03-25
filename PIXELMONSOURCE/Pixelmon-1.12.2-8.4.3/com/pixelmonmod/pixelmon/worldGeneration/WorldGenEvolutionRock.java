package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.enums.EnumEvolutionRock;
import com.pixelmonmod.pixelmon.items.PixelmonItemBlock;
import com.pixelmonmod.pixelmon.worldGeneration.structure.GeneratorHelper;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenEvolutionRock implements IWorldGenerator {
   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (GeneratorHelper.isOverworld(world) || GeneratorHelper.isUltraSpace(world)) {
         int xPos = random.nextInt(16) + chunkX * 16;
         int zPos = random.nextInt(16) + chunkZ * 16;
         int yPos = world.func_175645_m(new BlockPos(xPos, 0, zPos)).func_177956_o() - 1;
         Biome biome = world.func_180494_b(new BlockPos(xPos, 0, zPos));
         Block block = world.func_180495_p(new BlockPos(xPos, yPos - 1, zPos)).func_177230_c();
         if (block == Blocks.field_150349_c || block == Blocks.field_150346_d) {
            EnumEvolutionRock[] var12 = EnumEvolutionRock.values();
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               EnumEvolutionRock r = var12[var14];
               Biome[] var16 = r.biomes;
               int var17 = var16.length;

               for(int var18 = 0; var18 < var17; ++var18) {
                  Biome b = var16[var18];
                  if (b == biome && random.nextDouble() < 0.05) {
                     boolean canSpawn = true;

                     for(int ix = -1; ix < 2; ++ix) {
                        for(int iz = -1; iz < 2; ++iz) {
                           Block block2 = world.func_180495_p(new BlockPos(xPos + ix, yPos - 1, zPos + iz)).func_177230_c();
                           if (block2 != Blocks.field_150349_c && block2 != Blocks.field_150346_d) {
                              canSpawn = false;
                           }
                        }
                     }

                     if (canSpawn) {
                        if (r == EnumEvolutionRock.IcyRock) {
                           PixelmonItemBlock.setMultiBlocksWidth(new BlockPos(xPos, yPos + 1, zPos), EnumFacing.EAST, world, (MultiBlock)PixelmonBlocks.icyRock, PixelmonBlocks.icyRock, (EntityPlayer)null);
                        } else if (r == EnumEvolutionRock.MossyRock) {
                           PixelmonItemBlock.setMultiBlocksWidth(new BlockPos(xPos, yPos + 1, zPos), EnumFacing.EAST, world, (MultiBlock)PixelmonBlocks.mossyRock, PixelmonBlocks.mossyRock, (EntityPlayer)null);
                        }
                     }

                     return;
                  }
               }
            }
         }

      }
   }
}

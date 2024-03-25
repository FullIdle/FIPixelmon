package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.worldGeneration.structure.GeneratorHelper;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHiddenGrotto extends MapGenScatteredFeature implements IWorldGenerator {
   private static int lastChunk = 0;
   private static int lastX;
   private static int lastZ = 0;
   private static int MIN_DISTANCE = 512;
   private static int MIN_CHUNK = 128;
   private static HiddenGrotto groto = new HiddenGrotto();

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (PixelmonConfig.spawnGrotto) {
         int x = random.nextInt(16) + chunkX * 16;
         int z = random.nextInt(16) + chunkZ * 16;
         if (this.canGenerate(world, x, z)) {
            int y0 = this.lowerToGround(world, x, world.func_175645_m(new BlockPos(x, 0, z)).func_177956_o(), z);
            if (y0 < 0) {
               return;
            }

            int y1 = this.lowerToGround(world, x + 8, world.func_175645_m(new BlockPos(x + 8, 0, z)).func_177956_o(), z);
            if (y1 < 0) {
               return;
            }

            int y2 = this.lowerToGround(world, x, world.func_175645_m(new BlockPos(x, 0, z + 8)).func_177956_o(), z + 8);
            if (y2 < 0) {
               return;
            }

            int y3 = this.lowerToGround(world, x + 8, world.func_175645_m(new BlockPos(x + 8, 0, z + 8)).func_177956_o(), z + 8);
            if (y3 < 0) {
               return;
            }

            if (!this.waterCheck(world, x, y0, z) || !this.waterCheck(world, x + 8, y1, z) || !this.waterCheck(world, x, y2, z + 8) || !this.waterCheck(world, x + 8, y3, z + 8)) {
               return;
            }

            int y = Math.min(Math.min(y0, y1), Math.min(y2, y3));
            groto.generate(world, new BlockPos(x, y, z));
            lastX = x;
            lastZ = y;
         }

      }
   }

   private boolean canGenerate(World world, int x, int z) {
      if (!GeneratorHelper.isOverworld(world) && !GeneratorHelper.isUltraSpace(world)) {
         return false;
      } else {
         int distance = (int)Math.sqrt(Math.pow((double)(lastX - x), 2.0) + Math.pow((double)(lastZ - z), 2.0));
         if (distance >= MIN_DISTANCE && lastChunk % MIN_CHUNK == 0) {
            if (this.biomeOK(world.func_180494_b(new BlockPos(x, 0, z)))) {
               ++lastChunk;
               return true;
            } else {
               ++lastChunk;
               return false;
            }
         } else {
            ++lastChunk;
            return false;
         }
      }
   }

   private boolean biomeOK(Biome b) {
      if (BetterSpawnerConfig.INSTANCE == null) {
         return false;
      } else {
         Set forests = (Set)BetterSpawnerConfig.INSTANCE.cachedBiomeCategories.get("all forests");
         if (forests != null && !forests.isEmpty()) {
            return forests.contains(b) && b.func_150561_m() != TempCategory.COLD;
         } else {
            Pixelmon.LOGGER.warn("There is nothing in an 'all forests' biome category in BetterSpawnerConfig.json. This is preventing hidden grottos from spawning!");
            return false;
         }
      }
   }

   private boolean waterCheck(World world, int x, int y, int z) {
      Block underBlock = world.func_180495_p(new BlockPos(x, y - 1, z)).func_177230_c();
      return underBlock != Blocks.field_150358_i && underBlock != Blocks.field_150355_j && underBlock != Blocks.field_150392_bi;
   }

   private int lowerToGround(World world, int x, int y, int z) {
      int Y = y;
      Block underBlock = world.func_180495_p(new BlockPos(x, y - 1, z)).func_177230_c();
      if (y > 0 && y < 256) {
         while(underBlock == Blocks.field_150362_t || underBlock == Blocks.field_150364_r || world.func_175623_d(new BlockPos(x, Y - 1, z))) {
            --Y;
            underBlock = world.func_180495_p(new BlockPos(x, Y - 1, z)).func_177230_c();
            if (Y < 0) {
               --Y;
               break;
            }
         }
      }

      return Y;
   }
}

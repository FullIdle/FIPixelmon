package com.pixelmonmod.pixelmon.worldGeneration;

import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpace;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenPokeChest implements IWorldGenerator {
   private static int lastChunk = 0;
   private static int lastX;
   private static int lastZ = 0;
   private static int totalPlaced = 0;
   private static int normalPlaced = 0;
   private static int ultraPlaced = 0;
   private int x;
   private int y;
   private int z;
   private int distance;

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      if (PixelmonConfig.spawnNormal || PixelmonConfig.spawnHidden) {
         this.genBlocks(random, chunkX, chunkZ, world);
      }

   }

   private void genBlocks(Random random, int chunkX, int chunkZ, World world) {
      this.x = random.nextInt(16) + chunkX * 16;
      this.z = random.nextInt(16) + chunkZ * 16;
      this.y = world.func_175645_m(new BlockPos(this.x, 0, this.z)).func_177956_o();
      this.distance = (int)Math.sqrt(Math.pow((double)(lastX - this.x), 2.0) + Math.pow((double)(lastZ - this.z), 2.0));
      if (this.distance >= PixelmonConfig.spawnRate.getMinDistance() && lastChunk % PixelmonConfig.spawnRate.getMinChunk() == 0) {
         IBlockState underBlockState = world.func_180495_p(new BlockPos(this.x, this.y - 1, this.z));
         Block underBlock = underBlockState.func_177230_c();

         while(underBlock == Blocks.field_150362_t || underBlock == Blocks.field_150364_r || underBlockState.func_185904_a() == Material.field_151579_a) {
            --this.y;
            if (this.y <= 0) {
               return;
            }

            try {
               underBlockState = world.func_180495_p(new BlockPos(this.x, this.y - 1, this.z));
               underBlock = underBlockState.func_177230_c();
            } catch (Exception var11) {
               return;
            }
         }

         if (underBlockState.func_185904_a() != Material.field_151587_i && underBlockState.func_185904_a() != Material.field_151586_h) {
            Block block = PixelmonBlocks.pokeChest;
            if (world.field_73011_w.getDimension() == UltraSpace.DIM_ID) {
               block = PixelmonBlocks.beastChest;
            } else if (normalPlaced > 4) {
               if (ultraPlaced > 4) {
                  block = PixelmonBlocks.masterChest;
                  ultraPlaced = 0;
               } else {
                  block = PixelmonBlocks.ultraChest;
                  ++ultraPlaced;
               }

               normalPlaced = 0;
            } else {
               ++normalPlaced;
            }

            BlockPos currentPos = new BlockPos(this.x, this.y, this.z);
            TileEntity chestEntity;
            if (PixelmonConfig.spawnHidden && totalPlaced % PixelmonConfig.spawnRate.getHidden_frequency() == 0 && totalPlaced != 0 && block == PixelmonBlocks.pokeChest) {
               world.func_180501_a(currentPos, PixelmonBlocks.pokeChest.func_176223_P(), 18);
               chestEntity = world.func_175625_s(currentPos);
               if (!(chestEntity instanceof TileEntityPokeChest)) {
                  return;
               }

               ((TileEntityPokeChest)chestEntity).setVisibility(EnumPokechestVisibility.Hidden);
            } else if (PixelmonConfig.spawnNormal) {
               world.func_180501_a(currentPos, block.func_176223_P(), 18);
            } else if (PixelmonConfig.spawnHidden) {
               world.func_180501_a(currentPos, PixelmonBlocks.pokeChest.func_176223_P(), 18);
               chestEntity = world.func_175625_s(currentPos);
               if (chestEntity instanceof TileEntityPokeChest) {
                  ((TileEntityPokeChest)chestEntity).setVisibility(EnumPokechestVisibility.Hidden);
               }
            }

            chestEntity = world.func_175625_s(currentPos);
            if (chestEntity != null && chestEntity instanceof TileEntityPokeChest) {
               TileEntityPokeChest t = (TileEntityPokeChest)chestEntity;
               t.setChestOneTime(PixelmonConfig.spawnMode.isOneTimeUse());
               t.setDropOneTime(PixelmonConfig.spawnMode.isOncePerPlayer());
               t.setTimeEnabled(PixelmonConfig.spawnMode.isTimeEnabled());
               lastX = this.x;
               lastZ = this.z;
               ++totalPlaced;
            } else {
               world.func_175698_g(currentPos);
            }
         } else {
            --lastChunk;
         }
      }

      ++lastChunk;
   }
}

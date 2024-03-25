package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import com.pixelmonmod.pixelmon.worldGeneration.loot.PixelmonLootTableList;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenPyramidInverse extends WorldGenerator {
   private IBlockState BLOCK;

   public WorldGenPyramidInverse(IBlockState block) {
      super(false);
      this.BLOCK = block;
   }

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      int tiers = rand.nextInt(5) + 3 + rand.nextInt(2);
      if (position.func_177956_o() >= 1 && position.func_177956_o() + tiers + 1 <= 256) {
         BlockPos down = position.func_177977_b();

         for(int y = 0; y < tiers; ++y) {
            int width = (tiers - y) * 2 - 1;
            int sizing = (width - 1) / 2;

            for(int w = 0; w < width; ++w) {
               for(int x = -sizing; x <= sizing; ++x) {
                  for(int z = -sizing; z <= sizing; ++z) {
                     this.func_175903_a(worldIn, position.func_177982_a(x, -y, z), this.BLOCK);
                  }
               }
            }
         }

         double spawnChest = rand.nextDouble();
         if (spawnChest < 0.05) {
            worldIn.func_180501_a(position, Blocks.field_150486_ae.func_176223_P(), 2);
            TileEntity tileentity = worldIn.func_175625_s(position);
            if (tileentity instanceof TileEntityChest) {
               ((TileEntityChest)tileentity).func_189404_a(PixelmonLootTableList.ULTRA_DESERT, rand.nextLong());
            }
         }

         return true;
      } else {
         return false;
      }
   }
}

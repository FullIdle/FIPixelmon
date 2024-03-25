package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.generators;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenGlowstone extends WorldGenerator {
   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      if (!worldIn.func_175623_d(position)) {
         return false;
      } else if (worldIn.func_180495_p(position.func_177984_a()).func_177230_c() == Blocks.field_150350_a) {
         return false;
      } else {
         worldIn.func_180501_a(position, Blocks.field_150426_aN.func_176223_P(), 2);

         for(int i = 0; i < 1500; ++i) {
            BlockPos blockpos = position.func_177982_a(rand.nextInt(8) - rand.nextInt(8), -rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.func_175623_d(blockpos)) {
               int j = 0;
               EnumFacing[] var7 = EnumFacing.values();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  EnumFacing enumfacing = var7[var9];
                  if (worldIn.func_180495_p(blockpos.func_177972_a(enumfacing)).func_177230_c() == Blocks.field_150426_aN) {
                     ++j;
                  }

                  if (j > 1) {
                     break;
                  }
               }

               if (j == 1) {
                  worldIn.func_180501_a(blockpos, Blocks.field_150426_aN.func_176223_P(), 2);
               }
            }
         }

         return true;
      }
   }
}

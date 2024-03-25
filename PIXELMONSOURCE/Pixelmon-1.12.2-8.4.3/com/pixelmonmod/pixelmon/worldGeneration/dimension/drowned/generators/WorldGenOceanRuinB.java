package com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned.generators;

import com.pixelmonmod.pixelmon.worldGeneration.loot.PixelmonLootTableList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockPrismarine.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenOceanRuinB extends WorldGenerator {
   private static final IBlockState PRISMARINE;
   private static final IBlockState PRISMARINE_BRICKS;
   private static final IBlockState PRISMARINE_DARK;
   private static final IBlockState SEA_LANTERN;
   private static final Logger LOGGER;
   private static final ResourceLocation GUARDIAN;

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      int i = true;
      int j = rand.nextInt(2) + 2;
      int k = -j - 1;
      int l = j + 1;
      int i1 = true;
      int j1 = true;
      int k1 = rand.nextInt(2) + 2;
      int l1 = -k1 - 1;
      int i2 = k1 + 1;
      int j2 = 0;
      double ruinedFactor = MathHelper.func_151237_a(rand.nextDouble() - 0.25, 0.15, 0.5);

      int k3;
      int i4;
      int k4;
      BlockPos blockpos1;
      for(k3 = k; k3 <= l; ++k3) {
         for(i4 = -1; i4 <= 4; ++i4) {
            for(k4 = l1; k4 <= i2; ++k4) {
               blockpos1 = position.func_177982_a(k3, i4, k4);
               Material material = worldIn.func_180495_p(blockpos1).func_185904_a();
               boolean flag = material.func_76220_a();
               if ((k3 == k || k3 == l || k4 == l1 || k4 == i2) && i4 == 0 && worldIn.func_180495_p(blockpos1).func_177230_c() == Blocks.field_150355_j && worldIn.func_180495_p(blockpos1.func_177984_a()).func_177230_c() == Blocks.field_150355_j) {
                  ++j2;
               }
            }
         }
      }

      if (j2 >= 1 && j2 <= 100) {
         for(k3 = k; k3 <= l; ++k3) {
            for(i4 = 3; i4 >= -1; --i4) {
               for(k4 = l1; k4 <= i2; ++k4) {
                  blockpos1 = position.func_177982_a(k3, i4, k4);
                  if (worldIn.func_180495_p(blockpos1).func_177230_c() != Blocks.field_150486_ae) {
                     if (i4 == -1 && rand.nextInt(4) != 0) {
                        worldIn.func_180501_a(blockpos1, rand.nextDouble() < ruinedFactor ? Blocks.field_150355_j.func_176223_P() : PRISMARINE_BRICKS, 2);
                     } else if (i4 == -1 || k3 == k || k3 == l || k4 == l1 || k4 == i2) {
                        worldIn.func_180501_a(blockpos1, rand.nextDouble() < ruinedFactor ? Blocks.field_150355_j.func_176223_P() : PRISMARINE, 2);
                     }
                  }
               }
            }
         }

         for(k3 = 0; k3 < 2; ++k3) {
            for(i4 = 0; i4 < 3; ++i4) {
               k4 = position.func_177958_n() + rand.nextInt(j * 2 + 1) - j;
               int i5 = position.func_177956_o();
               int j5 = position.func_177952_p() + rand.nextInt(k1 * 2 + 1) - k1;
               BlockPos blockpos2 = new BlockPos(k4, i5, j5);
               if (worldIn.func_180495_p(blockpos2).func_177230_c() == Blocks.field_150355_j) {
                  int j3 = 0;
                  Iterator var23 = Plane.HORIZONTAL.iterator();

                  while(var23.hasNext()) {
                     EnumFacing enumfacing = (EnumFacing)var23.next();
                     if (worldIn.func_180495_p(blockpos2.func_177972_a(enumfacing)).func_185904_a().func_76220_a()) {
                        ++j3;
                     }
                  }

                  if (j3 == 1) {
                     worldIn.func_180501_a(blockpos2, Blocks.field_150486_ae.func_176458_f(worldIn, blockpos2, Blocks.field_150486_ae.func_176223_P()), 2);
                     TileEntity tileentity1 = worldIn.func_175625_s(blockpos2);
                     if (tileentity1 instanceof TileEntityChest) {
                        ((TileEntityChest)tileentity1).func_189404_a(PixelmonLootTableList.DROWNED_DUNGEON, rand.nextLong());
                     }
                     break;
                  }
               }
            }
         }

         worldIn.func_180501_a(position, Blocks.field_150474_ac.func_176223_P(), 2);
         TileEntity tileentity = worldIn.func_175625_s(position);
         if (tileentity instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner)tileentity).func_145881_a().func_190894_a(this.pickMobSpawner(rand));
         } else {
            LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", position.func_177958_n(), position.func_177956_o(), position.func_177952_p());
         }

         return true;
      } else {
         return false;
      }
   }

   private ResourceLocation pickMobSpawner(Random rand) {
      return GUARDIAN;
   }

   static {
      PRISMARINE = Blocks.field_180397_cI.func_176223_P().func_177226_a(BlockPrismarine.field_176332_a, EnumType.ROUGH);
      PRISMARINE_BRICKS = Blocks.field_180397_cI.func_176223_P().func_177226_a(BlockPrismarine.field_176332_a, EnumType.BRICKS);
      PRISMARINE_DARK = Blocks.field_180397_cI.func_176223_P().func_177226_a(BlockPrismarine.field_176332_a, EnumType.DARK);
      SEA_LANTERN = Blocks.field_180398_cJ.func_176223_P();
      LOGGER = LogManager.getLogger();
      GUARDIAN = EntityList.func_191306_a(EntityGuardian.class);
   }
}

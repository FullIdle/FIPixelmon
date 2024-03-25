package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class UltraSpaceWorldGenFixListener {
   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void onDecorate(DecorateBiomeEvent.Decorate event) {
      if (event.getWorld().field_73011_w.field_76577_b == UltraSpace.WORLD_TYPE && (event.getType() == EventType.LAKE_LAVA || event.getType() == EventType.LAKE_WATER)) {
         event.setResult(Result.DENY);
         int x = event.getRand().nextInt(16) + 8;
         int z = event.getRand().nextInt(16) + 8;
         int k5;
         int i17;
         if (event.getType() == EventType.LAKE_WATER) {
            for(k5 = 0; k5 < 50; ++k5) {
               i17 = event.getRand().nextInt(248) + 8;
               if (i17 > 0) {
                  int k19 = event.getRand().nextInt(i17);
                  BlockPos blockpos6 = event.getPos().func_177982_a(x, k19, z);
                  (new WorldGenLiquidsSafe(Blocks.field_150358_i)).func_180709_b(event.getWorld(), event.getRand(), blockpos6);
               }
            }
         } else {
            for(k5 = 0; k5 < 20; ++k5) {
               i17 = event.getRand().nextInt(event.getRand().nextInt(event.getRand().nextInt(240) + 8) + 8);
               BlockPos blockpos3 = event.getPos().func_177982_a(x, i17, z);
               (new WorldGenLiquidsSafe(Blocks.field_150356_k)).func_180709_b(event.getWorld(), event.getRand(), blockpos3);
            }
         }
      }

   }

   private static class WorldGenLiquidsSafe extends WorldGenLiquids {
      Block field_150521_a;

      public WorldGenLiquidsSafe(Block blockIn) {
         super(blockIn);
         this.field_150521_a = blockIn;
      }

      public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
         if (worldIn.func_180495_p(position.func_177984_a()).func_177230_c() != Blocks.field_150348_b) {
            return false;
         } else if (worldIn.func_180495_p(position.func_177977_b()).func_177230_c() != Blocks.field_150348_b) {
            return false;
         } else {
            IBlockState iblockstate = worldIn.func_180495_p(position);
            if (!iblockstate.func_177230_c().isAir(iblockstate, worldIn, position) && iblockstate.func_177230_c() != Blocks.field_150348_b) {
               return false;
            } else {
               int i = 0;
               if (worldIn.func_180495_p(position.func_177976_e()).func_177230_c() == Blocks.field_150348_b) {
                  ++i;
               }

               if (worldIn.func_180495_p(position.func_177974_f()).func_177230_c() == Blocks.field_150348_b) {
                  ++i;
               }

               if (worldIn.func_180495_p(position.func_177978_c()).func_177230_c() == Blocks.field_150348_b) {
                  ++i;
               }

               if (worldIn.func_180495_p(position.func_177968_d()).func_177230_c() == Blocks.field_150348_b) {
                  ++i;
               }

               int j = 0;
               if (worldIn.func_175623_d(position.func_177976_e())) {
                  ++j;
               }

               if (worldIn.func_175623_d(position.func_177974_f())) {
                  ++j;
               }

               if (worldIn.func_175623_d(position.func_177978_c())) {
                  ++j;
               }

               if (worldIn.func_175623_d(position.func_177968_d())) {
                  ++j;
               }

               if (i == 3 && j == 1) {
                  IBlockState iblockstate1 = this.field_150521_a.func_176223_P();
                  worldIn.func_180501_a(position, iblockstate1, 2);
                  iblockstate1.func_177230_c().func_180650_b(worldIn, position, iblockstate1, rand);
               }

               return true;
            }
         }
      }
   }
}

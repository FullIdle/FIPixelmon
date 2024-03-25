package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.tools.LineCalc;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Dig {
   public static ArrayList allowedBlocks = new ArrayList();

   public static MoveSkill createMoveSkill() {
      allowedBlocks = Lists.newArrayList(new Block[]{Blocks.field_150349_c, Blocks.field_150354_m, Blocks.field_150322_A, Blocks.field_150435_aG, Blocks.field_150348_b, Blocks.field_150351_n, Blocks.field_150391_bh, Blocks.field_150346_d});
      MoveSkill moveSkill = (new MoveSkill("dig")).setName("pixelmon.moveskill.dig.name").describe("pixelmon.moveskill.dig.description1", "pixelmon.moveskill.dig.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/dig.png")).setAnyMoves("Dig").setUsePP(true);
      moveSkill.setBehaviourBlockTarget((pixelmon, tup) -> {
         if (!PixelmonConfig.allowDestructiveExternalMoves) {
            return -1;
         } else {
            EnumFacing sideHit = (EnumFacing)tup.func_76340_b();
            BlockPos pos = (BlockPos)tup.func_76341_a();
            int depth = 74 - (int)(((float)pixelmon.getPokemonData().getStat(StatsType.Attack) - 20.0F) / 180.0F * 74.0F);
            if (depth < 2) {
               depth = 2;
            }

            int maxOff = 1;
            if (pixelmon.getPokemonData().getStat(StatsType.Attack) > 150) {
               maxOff = 2;
            }

            if (pixelmon.getPokemonData().getStat(StatsType.Attack) > 300) {
               maxOff = 3;
            }

            if (pos.func_177956_o() < depth) {
               pixelmon.field_70170_p.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187689_f, SoundCategory.NEUTRAL, 0.3F, 0.1F);
            } else {
               int yOff;
               int zOff;
               if (sideHit == EnumFacing.UP) {
                  for(yOff = -1 * maxOff; yOff <= maxOff; ++yOff) {
                     for(zOff = -1 * maxOff; zOff <= maxOff; ++zOff) {
                        doHarvest(pixelmon, pos.func_177982_a(yOff, 0, zOff));
                     }
                  }
               } else if (sideHit == EnumFacing.DOWN) {
                  for(yOff = -1 * maxOff; yOff <= maxOff; ++yOff) {
                     for(zOff = -1 * maxOff; zOff <= maxOff; ++zOff) {
                        doHarvest(pixelmon, pos.func_177982_a(yOff, 0, zOff));
                     }
                  }
               } else if (sideHit == EnumFacing.SOUTH) {
                  for(yOff = -1 * maxOff; yOff <= maxOff; ++yOff) {
                     for(zOff = -1 * maxOff; zOff <= maxOff; ++zOff) {
                        doHarvest(pixelmon, pos.func_177982_a(yOff, zOff, 0));
                     }
                  }
               } else if (sideHit == EnumFacing.NORTH) {
                  for(yOff = -1 * maxOff; yOff <= maxOff; ++yOff) {
                     for(zOff = -1 * maxOff; zOff <= maxOff; ++zOff) {
                        doHarvest(pixelmon, pos.func_177982_a(yOff, zOff, 0));
                     }
                  }
               } else if (sideHit == EnumFacing.EAST) {
                  for(yOff = -1 * maxOff; yOff <= maxOff; ++yOff) {
                     for(zOff = -1 * maxOff; zOff <= maxOff; ++zOff) {
                        doHarvest(pixelmon, pos.func_177982_a(0, yOff, zOff));
                     }
                  }
               } else if (sideHit == EnumFacing.WEST) {
                  for(yOff = -1 * maxOff; yOff <= maxOff; ++yOff) {
                     for(zOff = -1 * maxOff; zOff <= maxOff; ++zOff) {
                        doHarvest(pixelmon, pos.func_177982_a(0, yOff, zOff));
                     }
                  }
               }

               pixelmon.field_70170_p.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187571_bR, SoundCategory.NEUTRAL, 1.0F, 0.1F);
            }

            float speed = (float)pixelmon.getPokemonData().getStat(StatsType.Speed);
            return (int)(300.0F * (1.0F - LineCalc.lerp(speed, 1.0F, 200.0F, 0.0F, 0.5F)));
         }
      });
      return moveSkill;
   }

   private static void doHarvest(EntityPixelmon user, BlockPos pos) {
      IBlockState state = user.field_70170_p.func_180495_p(pos);
      Block block = state.func_177230_c();
      if (allowedBlocks.contains(block)) {
         EntityPlayerMP player = (EntityPlayerMP)user.func_70902_q();
         boolean flag = block.canHarvestBlock(user.field_70170_p, pos, player);
         if (flag && removeBlock(pos, user.field_70170_p, player, true)) {
            block.func_176226_b(user.field_70170_p, pos, state, 0);
         }
      }

   }

   private static boolean removeBlock(BlockPos pos, World theWorld, EntityPlayerMP playerMP, boolean canHarvest) {
      IBlockState iblockstate = theWorld.func_180495_p(pos);
      boolean flag = false;
      Block block = iblockstate.func_177230_c();
      block.func_176208_a(theWorld, pos, iblockstate, playerMP);
      flag = block.removedByPlayer(iblockstate, theWorld, pos, playerMP, canHarvest);
      if (flag) {
         block.func_176206_d(theWorld, pos, iblockstate);
      }

      return flag;
   }
}

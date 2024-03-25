package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.tools.LineCalc;
import com.pixelmonmod.pixelmon.util.Scheduling;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Cut {
   public static int maximumLogs = 100;
   public static int ticksBetweenChops = 2;
   public static ArrayList checkDirections;

   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("cut")).setName("pixelmon.moveskill.cut.name").describe("pixelmon.moveskill.cut.description1", "pixelmon.moveskill.cut.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/cut.png")).setAnyMoves("Cut").setUsePP(true).setRange(8);
      moveSkill.setBehaviourBlockTarget((pixelmon, tup) -> {
         if (!PixelmonConfig.allowDestructiveExternalMoves) {
            return -1;
         } else {
            BlockPos pos = (BlockPos)tup.func_76341_a();
            World world = pixelmon.field_70170_p;
            if (!world.func_180495_p(pos).func_177230_c().isWood(world, pos)) {
               return -1;
            } else if (world.func_180495_p(pos).func_177229_b(BlockLog.field_176299_a) != EnumAxis.Y) {
               return -1;
            } else {
               ArrayList logs = findTreeLogs(world, pos);
               int logCount = logs.size();
               if (logs.size() > maximumLogs) {
                  ChatHandler.sendChat(pixelmon.func_70902_q(), "pixelmon.moveskill.cut.toomany");
                  return -1;
               } else {
                  float maxLogs = LineCalc.lerp((float)pixelmon.getPokemonData().getStat(StatsType.Attack), 1.0F, 300.0F, 1.0F, (float)maximumLogs);
                  if (maxLogs <= (float)logs.size()) {
                     ChatHandler.sendChat(pixelmon.func_70902_q(), "pixelmon.moveskill.cut.fail", pixelmon.getPokemonData().getDisplayName());
                     return -1;
                  } else {
                     Scheduling.schedule(ticksBetweenChops, (task) -> {
                        if (!logs.isEmpty()) {
                           EntityPlayer player = (EntityPlayerMP)pixelmon.func_70902_q();
                           if (player == null) {
                              return;
                           }

                           BlockPos log = (BlockPos)logs.remove(0);
                           IBlockState state = world.func_180495_p(log);
                           ItemStack hand = player.func_184582_a(EntityEquipmentSlot.MAINHAND);

                           try {
                              player.func_184201_a(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
                              if (((EntityPlayerMP)pixelmon.func_70902_q()).field_71134_c.func_180237_b(log)) {
                                 state.func_177230_c().func_176226_b(world, log, state, 2);
                              }
                           } finally {
                              player.func_184201_a(EntityEquipmentSlot.MAINHAND, hand);
                           }
                        }

                        if (logs.isEmpty()) {
                           task.repeats = false;
                        }

                     }, true);
                     float speed = (float)pixelmon.getPokemonData().getStat(StatsType.Speed);
                     return (int)(200.0F + (float)(logCount * 20) * (1.5F - LineCalc.ratio(speed, 1.0F, 200.0F)));
                  }
               }
            }
         }
      });
      return moveSkill;
   }

   private static ArrayList findTreeLogs(World world, BlockPos pos) {
      ArrayList logs = new ArrayList();
      ArrayList newLogs = new ArrayList();
      newLogs.add(pos);

      while(!newLogs.isEmpty()) {
         addAdjacentLogs(world, (BlockPos)newLogs.get(0), newLogs, logs);
         logs.add(newLogs.get(0));
         newLogs.remove(0);
      }

      return logs;
   }

   private static void addAdjacentLogs(World world, BlockPos pos, ArrayList newLogs, ArrayList logs) {
      Iterator var4 = checkDirections.iterator();

      while(var4.hasNext()) {
         EnumFacing[] facings = (EnumFacing[])var4.next();
         BlockPos loc = pos;
         EnumFacing[] var7 = facings;
         int var8 = facings.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            EnumFacing f = var7[var9];
            loc = loc.func_177972_a(f);
         }

         if (world.func_180495_p(loc).func_177230_c().isWood(world, loc) && !hasLog(logs, loc) && !hasLog(newLogs, loc)) {
            newLogs.add(loc);
         }
      }

   }

   private static boolean hasLog(ArrayList logs, BlockPos loc) {
      Iterator var2 = logs.iterator();

      BlockPos log;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         log = (BlockPos)var2.next();
      } while(!log.equals(loc));

      return true;
   }

   static {
      checkDirections = Lists.newArrayList(new EnumFacing[][]{{EnumFacing.NORTH}, {EnumFacing.SOUTH}, {EnumFacing.EAST}, {EnumFacing.WEST}, {EnumFacing.UP}, {EnumFacing.UP, EnumFacing.NORTH}, {EnumFacing.UP, EnumFacing.SOUTH}, {EnumFacing.UP, EnumFacing.EAST}, {EnumFacing.UP, EnumFacing.WEST}});
   }
}

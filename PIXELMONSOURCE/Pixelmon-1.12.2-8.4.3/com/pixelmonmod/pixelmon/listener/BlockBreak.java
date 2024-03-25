package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBell;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockBreak {
   @SubscribeEvent
   public static void blockBreak(BlockEvent.BreakEvent event) {
      if (!event.getState().func_177230_c().equals(PixelmonBlocks.clearBell) && !event.getState().func_177230_c().equals(PixelmonBlocks.tidalBell)) {
         BlockPos downOnePos = event.getPos().func_177982_a(0, -1, 0);
         IBlockState downOne = event.getWorld().func_180495_p(downOnePos);
         if ((downOne.func_177230_c().equals(PixelmonBlocks.clearBell) || downOne.func_177230_c().equals(PixelmonBlocks.tidalBell)) && event.getWorld().func_175625_s(downOnePos) != null && event.getWorld().func_175625_s(downOnePos) instanceof TileEntityBell) {
            TileEntityBell bell = (TileEntityBell)event.getWorld().func_175625_s(downOnePos);
            if (!event.getPlayer().func_184812_l_() && bell != null && bell.spawning) {
               event.setCanceled(true);
            } else {
               event.getWorld().func_175655_b(downOnePos, true);
            }
         }
      } else if (event.getWorld().func_175625_s(event.getPos()) != null && event.getWorld().func_175625_s(event.getPos()) instanceof TileEntityBell) {
         TileEntityBell bell = (TileEntityBell)event.getWorld().func_175625_s(event.getPos());
         if (!event.getPlayer().func_184812_l_() && bell != null && bell.spawning) {
            event.setCanceled(true);
         }
      }

   }
}

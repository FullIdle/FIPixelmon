package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.blocks.machines.BlockElevator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ElevatorEvents {
   @SubscribeEvent
   public static void onJump(LivingEvent.LivingJumpEvent event) {
      if (event.getEntity() instanceof EntityPlayerMP) {
         Block block = event.getEntity().field_70170_p.func_180495_p(event.getEntity().func_180425_c().func_177977_b()).func_177230_c();
         if (block instanceof BlockElevator) {
            ((BlockElevator)block).takeElevator(event.getEntity().field_70170_p, event.getEntity().func_180425_c().func_177977_b(), (EntityPlayerMP)event.getEntity(), true);
         }
      }
   }
}

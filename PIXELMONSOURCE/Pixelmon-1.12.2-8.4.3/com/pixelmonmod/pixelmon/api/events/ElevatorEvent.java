package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.machines.BlockElevator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ElevatorEvent extends Event {
   public final EntityPlayerMP player;
   public final BlockElevator elevator;
   public final boolean goingUp;
   private BlockPos destination;

   public ElevatorEvent(EntityPlayerMP player, BlockElevator elevator, boolean goingUp, BlockPos destination) {
      this.player = player;
      this.elevator = elevator;
      this.goingUp = goingUp;
      this.destination = destination;
   }

   public BlockPos getDestination() {
      return this.destination;
   }

   public void setDestination(BlockPos destination) {
      if (destination != null) {
         this.destination = destination;
      }

   }
}

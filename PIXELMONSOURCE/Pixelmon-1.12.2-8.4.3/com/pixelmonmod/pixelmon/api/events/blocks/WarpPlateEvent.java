package com.pixelmonmod.pixelmon.api.events.blocks;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class WarpPlateEvent extends Event {
   private final Entity entity;
   private BlockPos pos;

   public WarpPlateEvent(Entity entity, BlockPos pos) {
      this.entity = entity;
      this.pos = pos;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public BlockPos getWarpPosition() {
      return this.pos;
   }

   public void setWarpPosition(BlockPos pos) {
      this.pos = pos;
   }
}

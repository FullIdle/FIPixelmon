package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.machines.BlockShrine;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityShrine;
import com.pixelmonmod.pixelmon.enums.EnumShrine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PlayerActivateShrineEvent extends Event {
   public final BlockShrine block;
   public final EntityPlayerMP player;
   public final EnumShrine shrineType;
   public final TileEntityShrine shrine;
   public final BlockPos position;

   public PlayerActivateShrineEvent(EntityPlayerMP player, BlockShrine block, EnumShrine shrineType, TileEntityShrine shrine, BlockPos position) {
      this.player = player;
      this.block = block;
      this.shrineType = shrineType;
      this.shrine = shrine;
      this.position = position;
   }
}

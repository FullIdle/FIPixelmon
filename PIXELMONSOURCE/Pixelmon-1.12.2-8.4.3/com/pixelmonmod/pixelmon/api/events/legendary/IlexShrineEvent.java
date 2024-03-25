package com.pixelmonmod.pixelmon.api.events.legendary;

import com.pixelmonmod.pixelmon.blocks.machines.BlockIlexShrine;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityIlexShrine;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class IlexShrineEvent extends Event {
   public final BlockIlexShrine block;
   public final EntityPlayerMP player;
   public final EntityPixelmon pixelmonEntity;
   public final TileEntityIlexShrine shrine;
   public final BlockPos position;

   public IlexShrineEvent(EntityPlayerMP player, BlockIlexShrine block, EntityPixelmon pixelmonEntity, TileEntityIlexShrine shrine, BlockPos position) {
      this.block = block;
      this.player = player;
      this.pixelmonEntity = pixelmonEntity;
      this.shrine = shrine;
      this.position = position;
   }
}

package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.spawning.BlockSpawningHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PixelmonBlockTriggeredBattleEvent extends Event {
   public final BlockSpawningHandler handler;
   public final World world;
   public final BlockPos pos;
   public final EntityPlayerMP player;
   public final EntityPixelmon pixelmon;
   public final EnumBattleStartTypes startType;

   public PixelmonBlockTriggeredBattleEvent(BlockSpawningHandler handler, World world, BlockPos pos, EntityPlayerMP player, EntityPixelmon entityPixelmon, EnumBattleStartTypes startType) {
      this.handler = handler;
      this.world = world;
      this.pos = pos;
      this.player = player;
      this.pixelmon = entityPixelmon;
      this.startType = startType;
   }
}

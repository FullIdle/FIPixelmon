package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PixelmonBlockStartingBattleEvent extends Event {
   public final World worldIn;
   public final BlockPos pos;
   public final EntityPlayerMP player;
   public final EnumBattleStartTypes startType;
   public final EntityPixelmon initialisingPixelmon;
   public final EntityPixelmon wildPixelmon1;
   public final EntityPixelmon wildPixelmon2;

   public PixelmonBlockStartingBattleEvent(World worldIn, BlockPos pos, EntityPlayerMP player, EnumBattleStartTypes startType, EntityPixelmon fightingPokemon, EntityPixelmon pixelmon1, EntityPixelmon pixelmon2) {
      this.worldIn = worldIn;
      this.pos = pos;
      this.player = player;
      this.startType = startType;
      this.initialisingPixelmon = fightingPokemon;
      this.wildPixelmon1 = pixelmon1;
      this.wildPixelmon2 = pixelmon2;
   }
}

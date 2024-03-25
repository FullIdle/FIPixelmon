package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/** @deprecated */
@Deprecated
@Cancelable
public class PokeLootClaimedEvent extends Event {
   /** @deprecated */
   @Deprecated
   public TileEntityPokeChest chest;
   /** @deprecated */
   @Deprecated
   public EntityPlayerMP player;

   /** @deprecated */
   @Deprecated
   public PokeLootClaimedEvent(EntityPlayerMP player, TileEntityPokeChest tile) {
      this.player = player;
      this.chest = tile;
   }
}

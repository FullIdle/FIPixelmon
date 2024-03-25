package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.enums.DeleteType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PixelmonDeletedEvent extends Event {
   public final EntityPlayerMP player;
   public final Pokemon pokemon;
   public final DeleteType deleteType;

   public PixelmonDeletedEvent(EntityPlayerMP player, Pokemon pokemon, DeleteType deleteType) {
      this.player = player;
      this.pokemon = pokemon;
      this.deleteType = deleteType;
   }
}

package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PixelmonSendOutEvent extends Event {
   public final EntityPlayerMP player;
   public final Pokemon pokemon;

   public PixelmonSendOutEvent(EntityPlayerMP player, Pokemon pokemon) {
      this.player = player;
      this.pokemon = pokemon;
   }
}

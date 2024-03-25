package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PixelmonReceivedEvent extends Event {
   public final EntityPlayerMP player;
   public final ReceiveType receiveType;
   public final Pokemon pokemon;

   public PixelmonReceivedEvent(EntityPlayerMP player, ReceiveType recievedType, Pokemon pokemon) {
      this.player = player;
      this.receiveType = recievedType;
      this.pokemon = pokemon;
   }
}

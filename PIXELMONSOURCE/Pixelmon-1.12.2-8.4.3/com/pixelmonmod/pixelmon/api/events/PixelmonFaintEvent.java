package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PixelmonFaintEvent extends Event {
   public final EntityPlayerMP player;
   public final EntityPixelmon pokemon;

   public PixelmonFaintEvent(EntityPlayerMP player, EntityPixelmon pokemon) {
      this.player = player;
      this.pokemon = pokemon;
   }
}

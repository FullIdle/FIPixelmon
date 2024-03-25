package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class PixelmonUpdateEvent extends Event {
   public final EntityPixelmon pokemon;
   public final TickEvent.Phase phase;

   public PixelmonUpdateEvent(EntityPixelmon pokemon, TickEvent.Phase phase) {
      this.pokemon = pokemon;
      this.phase = phase;
   }

   public boolean isCancelable() {
      return this.phase != Phase.END;
   }
}

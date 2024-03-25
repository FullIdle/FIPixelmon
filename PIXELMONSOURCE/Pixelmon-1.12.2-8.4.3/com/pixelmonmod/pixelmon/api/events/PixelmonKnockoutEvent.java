package com.pixelmonmod.pixelmon.api.events;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PixelmonKnockoutEvent extends Event {
   public final PixelmonWrapper source;
   public final PixelmonWrapper pokemon;

   public PixelmonKnockoutEvent(PixelmonWrapper source, PixelmonWrapper pokemon) {
      this.source = source;
      this.pokemon = pokemon;
   }
}

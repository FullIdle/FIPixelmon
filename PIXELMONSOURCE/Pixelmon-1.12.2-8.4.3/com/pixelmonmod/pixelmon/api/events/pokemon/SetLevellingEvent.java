package com.pixelmonmod.pixelmon.api.events.pokemon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SetLevellingEvent extends Event {
   public final Pokemon pokemon;
   public boolean doesLevel;

   public SetLevellingEvent(Pokemon pokemon, boolean doesLevel) {
      this.doesLevel = doesLevel;
      this.pokemon = pokemon;
   }
}

package com.pixelmonmod.tcg.api.events;

import java.util.List;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class StartGameEvent extends Event {
   private final List players;

   public StartGameEvent(List players) {
      this.players = players;
   }

   public List getPlayers() {
      return this.players;
   }
}

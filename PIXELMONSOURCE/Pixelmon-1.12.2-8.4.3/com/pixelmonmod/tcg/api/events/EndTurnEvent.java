package com.pixelmonmod.tcg.api.events;

import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EndTurnEvent extends Event {
   private final PlayerServerState player;
   private final int turn;

   public EndTurnEvent(PlayerServerState player, int turn) {
      this.player = player;
      this.turn = turn;
   }

   public int getTurn() {
      return this.turn;
   }

   public PlayerServerState getPlayer() {
      return this.player;
   }
}

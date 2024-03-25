package com.pixelmonmod.tcg.api.events;

import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EndGameEvent extends Event {
   private final PlayerServerState winner;
   private final PlayerServerState loser;
   private final boolean tiedGame;

   public EndGameEvent(PlayerServerState winner, PlayerServerState loser, boolean tiedGame) {
      this.winner = winner;
      this.loser = loser;
      this.tiedGame = tiedGame;
   }

   public EntityPlayerMP getWinnerPlayer() {
      return (EntityPlayerMP)this.winner.getEntityPlayer();
   }

   public EntityPlayerMP getLoserPlayer() {
      return (EntityPlayerMP)this.loser.getEntityPlayer();
   }

   public PlayerServerState getWinner() {
      return this.winner;
   }

   public PlayerServerState getLoser() {
      return this.loser;
   }

   public boolean isTiedGame() {
      return this.tiedGame;
   }
}

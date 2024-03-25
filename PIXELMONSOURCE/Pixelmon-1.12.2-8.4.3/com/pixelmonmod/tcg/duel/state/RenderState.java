package com.pixelmonmod.tcg.duel.state;

public class RenderState {
   private GamePhase gamePhase;
   private int currentTurn;
   private PlayerClientOpponentState[] players;

   public GamePhase getGamePhase() {
      return this.gamePhase;
   }

   public void setGamePhase(GamePhase gamePhase) {
      this.gamePhase = gamePhase;
   }

   public int getCurrentTurn() {
      return this.currentTurn;
   }

   public void setCurrentTurn(int currentTurn) {
      this.currentTurn = currentTurn;
   }

   public PlayerClientOpponentState[] getPlayers() {
      return this.players;
   }

   public void setPlayers(PlayerClientOpponentState[] players) {
      this.players = players;
   }
}

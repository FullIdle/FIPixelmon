package com.pixelmonmod.tcg.duel.state;

public enum GamePhase {
   FlippingCoin,
   PreMatch,
   FirstTurn,
   NormalTurn,
   BetweenTurns;

   public boolean after(GamePhase phase) {
      return this.ordinal() > phase.ordinal();
   }
}

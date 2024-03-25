package com.pixelmonmod.tcg.duel.state;

import java.util.List;

public class CoinFlipState {
   protected int playerIndex = -1;
   protected List results = null;

   public CoinFlipState(List results, Integer playerIndex) {
      this.results = results;
      this.playerIndex = playerIndex;
   }

   public int getPlayerIndex() {
      return this.playerIndex;
   }

   public List getResults() {
      return this.results;
   }
}

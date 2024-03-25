package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.log.DuelLog;

public abstract class GameCommonState {
   protected ImmutableCard stadiumCard;
   protected int turnCount;
   protected GamePhase gamePhase;
   protected CoinFlipState coinFlip;
   protected DuelLog log;
   private boolean isShadowGame = false;

   public void initialize() {
      this.stadiumCard = null;
      this.turnCount = 0;
      this.gamePhase = GamePhase.FirstTurn;
      this.log = new DuelLog();
   }

   public ImmutableCard getStadiumCard() {
      return this.stadiumCard;
   }

   public void setStadiumCard(ImmutableCard stadiumCard) {
      this.stadiumCard = stadiumCard;
   }

   public int getTurnCount() {
      return this.turnCount;
   }

   public void setTurnCount(int turnCount) {
      this.turnCount = turnCount;
   }

   public GamePhase getGamePhase() {
      return this.gamePhase;
   }

   public void setGamePhase(GamePhase gamePhase) {
      if (this.gamePhase != gamePhase) {
         this.gamePhase = gamePhase;
      }

   }

   public CoinFlipState getCoinFlip() {
      return this.coinFlip;
   }

   public void setCoinFlip(CoinFlipState coinFlip) {
      this.coinFlip = coinFlip;
   }

   public DuelLog getLog() {
      return this.log;
   }

   public void setLog(DuelLog log) {
      this.log = log;
   }
}

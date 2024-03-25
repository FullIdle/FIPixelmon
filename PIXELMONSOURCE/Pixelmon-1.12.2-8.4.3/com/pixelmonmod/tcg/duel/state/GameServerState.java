package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.duel.attack.PendingAttack;
import com.pixelmonmod.tcg.duel.log.DuelLog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;

public class GameServerState extends GameCommonState {
   private boolean gameInProgress;
   private Map spectators = new HashMap();
   private PlayerServerState[] players = new PlayerServerState[2];
   private int currentTurn;
   private int revealedCoinFlipResults = 0;
   private PokemonCardState pendingAbility = null;
   private PendingAttack pendingAttack = null;
   private int currentEffectIndex = -1;
   private List effectsParameters;
   private PokemonCardState burningCard = null;
   private PokemonCardState sleepingCard = null;
   private Queue resolvingConditionCards = null;
   private boolean handledPoisoned = false;
   private boolean handledBurned = false;
   private boolean handledAsleep = false;
   private boolean handledParalyzed = false;
   private boolean handledConfusedFlip = false;
   private List delayEffects = new ArrayList();

   public GameServerState() {
      this.initialize();
   }

   public void initialize() {
      super.initialize();
      this.gameInProgress = false;
      this.players = new PlayerServerState[2];
      this.currentTurn = 0;
      this.initLog();
   }

   public void initLog() {
      if (this.players[1] != null && this.players[0] != null) {
         this.log = new DuelLog();
      }

   }

   public boolean hasPlayer(EntityPlayer player) {
      return this.players[0] != null && this.players[0].getEntityPlayer() == player || this.players[1] != null && this.players[1].getEntityPlayer() == player;
   }

   public PlayerServerState[] getPlayers() {
      return this.players;
   }

   public boolean isGameInProgress() {
      return this.gameInProgress;
   }

   public void setGameInProgress(boolean gameInProgress) {
      this.gameInProgress = gameInProgress;
   }

   public Map getSpectators() {
      return this.spectators;
   }

   public PlayerServerState getPlayer(int index) {
      return this.players[index];
   }

   public PlayerServerState getCurrentPlayer() {
      return this.players[this.currentTurn];
   }

   public PlayerServerState getCurrentOpponent() {
      return this.players[this.getNextTurn()];
   }

   public PlayerServerState getPlayer(EntityPlayer player) {
      if (this.players[0] != null && this.players[0].getEntityPlayer() == player) {
         return this.players[0];
      } else {
         return this.players[1] != null && this.players[1].getEntityPlayer() == player ? this.players[1] : null;
      }
   }

   public PlayerServerState getOpponent(int index) {
      return this.players[(index + 1) % 2];
   }

   public PlayerServerState getOpponent(PlayerServerState player) {
      return this.players[0] == player ? this.players[1] : this.players[0];
   }

   public void setPlayer(int index, PlayerServerState player) {
      this.players[index] = player;
   }

   public int getCurrentTurn() {
      return this.currentTurn;
   }

   public int getTurn(PlayerServerState p) {
      if (this.players[0] == p) {
         return 0;
      } else {
         return this.players[1] == p ? 1 : -1;
      }
   }

   public int getNextTurn() {
      return (this.currentTurn + 1) % 2;
   }

   public void setCurrentTurn(int currentTurn) {
      this.currentTurn = currentTurn;
   }

   public boolean isCurrentTurn(PlayerServerState player) {
      return this.players[this.currentTurn] == player;
   }

   public int getRevealedCoinFlipResults() {
      return this.revealedCoinFlipResults;
   }

   public void setRevealedCoinFlipResults(int revealedCoinFlipResults) {
      this.revealedCoinFlipResults = revealedCoinFlipResults;
   }

   public PokemonCardState getPendingAbility() {
      return this.pendingAbility;
   }

   public void setPendingAbility(PokemonCardState pendingAbility) {
      this.pendingAbility = pendingAbility;
   }

   public PendingAttack getPendingAttack() {
      return this.pendingAttack;
   }

   public void setPendingAttack(PendingAttack pendingAttack) {
      this.pendingAttack = pendingAttack;
      this.currentEffectIndex = -1;
      this.effectsParameters = null;
   }

   public PokemonCardState getBurningCard() {
      return this.burningCard;
   }

   public void setBurningCard(PokemonCardState burningCard) {
      this.burningCard = burningCard;
   }

   public PokemonCardState getSleepingCard() {
      return this.sleepingCard;
   }

   public void setSleepingCard(PokemonCardState sleepingCard) {
      this.sleepingCard = sleepingCard;
   }

   public boolean isHandledPoisoned() {
      return this.handledPoisoned;
   }

   public void setHandledPoisoned(boolean handledPoisoned) {
      this.handledPoisoned = handledPoisoned;
   }

   public boolean isHandledBurned() {
      return this.handledBurned;
   }

   public void setHandledBurned(boolean handledBurned) {
      this.handledBurned = handledBurned;
   }

   public boolean isHandledAsleep() {
      return this.handledAsleep;
   }

   public void setHandledAsleep(boolean handledAsleep) {
      this.handledAsleep = handledAsleep;
   }

   public boolean isHandledParalyzed() {
      return this.handledParalyzed;
   }

   public void setHandledParalyzed(boolean handledParalyzed) {
      this.handledParalyzed = handledParalyzed;
   }

   public boolean isHandledConfusedFlip() {
      return this.handledConfusedFlip;
   }

   public void setHandledConfusedFlip(boolean handledConfusedFlip) {
      this.handledConfusedFlip = handledConfusedFlip;
   }

   public Queue getResolvingConditionCards() {
      return this.resolvingConditionCards;
   }

   public void setResolvingConditionCards(Queue resolvingConditionCards) {
      this.resolvingConditionCards = resolvingConditionCards;
   }

   public int getCurrentEffectIndex() {
      return this.currentEffectIndex;
   }

   public void setCurrentEffectIndex(int currentEffectIndex) {
      this.currentEffectIndex = currentEffectIndex;
   }

   public List getEffectsParameters() {
      return this.effectsParameters;
   }

   public void setEffectsParameters(List effectsParameters) {
      this.effectsParameters = effectsParameters;
   }

   public List getDelayEffects() {
      List expired = (List)this.delayEffects.stream().filter((e) -> {
         return e.getTurn() < this.turnCount;
      }).collect(Collectors.toList());
      this.delayEffects.removeAll(expired);
      return this.delayEffects;
   }

   public void addDelayEffect(DelayEffect delayEffect) {
      this.delayEffects.add(delayEffect);
   }

   public boolean exceedTimeLimit() {
      Date counterEndTime = this.getPlayer(this.currentTurn).getCounterEndTime();
      return counterEndTime != null && Calendar.getInstance().getTime().getTime() > counterEndTime.getTime();
   }
}

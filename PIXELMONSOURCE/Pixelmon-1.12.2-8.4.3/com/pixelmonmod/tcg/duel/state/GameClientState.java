package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.log.DuelLog;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GameClientState extends GameCommonState {
   private int playerIndex;
   private boolean isMyTurn;
   private PlayerClientMyState me;
   private PlayerClientOpponentState opponent;

   public GameClientState() {
      this.initialize();
   }

   public GameClientState(GameServerState server) {
      this.initialize();
      this.gamePhase = server.getGamePhase();
      this.playerIndex = server.getCurrentTurn();
      this.isMyTurn = true;
      this.me = new PlayerClientMyState(server.getPlayer(server.getCurrentTurn()), server.getGamePhase(), server, false);
      this.opponent = new PlayerClientOpponentState(server.getPlayer(server.getNextTurn()), server.getGamePhase(), server, false);
      this.turnCount = server.getTurnCount();
      this.log = new DuelLog();
   }

   public void initialize() {
      super.initialize();
      this.playerIndex = 0;
      this.isMyTurn = false;
      this.me = null;
      this.opponent = null;
   }

   public int getPlayerIndex() {
      return this.playerIndex;
   }

   public void setPlayerIndex(int playerIndex) {
      this.playerIndex = playerIndex;
   }

   public PlayerClientMyState getMe() {
      return this.me;
   }

   public void setMe(PlayerClientMyState me) {
      this.me = me;
   }

   public PlayerClientOpponentState getOpponent() {
      return this.opponent;
   }

   public void setOpponent(PlayerClientOpponentState opponent) {
      this.opponent = opponent;
   }

   public boolean isMyTurn() {
      return this.isMyTurn;
   }

   public void setMyTurn(boolean myTurn) {
      this.isMyTurn = myTurn;
   }

   public ImmutableCard getStadiumCard() {
      return this.stadiumCard;
   }

   public void setStadiumCard(ImmutableCard stadiumCard) {
      this.stadiumCard = stadiumCard;
   }

   public boolean isDisablingEvolution() {
      List players = Arrays.asList(this.me, this.opponent);
      Iterator var2 = players.iterator();

      while(var2.hasNext()) {
         PlayerCommonState player = (PlayerCommonState)var2.next();
         Iterator var4 = player.getActiveAndBenchCards().iterator();

         while(var4.hasNext()) {
            PokemonCardState pokemon = (PokemonCardState)var4.next();
            if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null && pokemon.getAbility().getEffect().disableEvolution(pokemon, this)) {
               return true;
            }

            if (pokemon.getHiddenAbility() != null && pokemon.getHiddenAbility().getEffect() != null && pokemon.getHiddenAbility().getEffect().disableEvolution(pokemon, this)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isDisablingEvolution(PokemonCardState affecting) {
      List players = Arrays.asList(this.me, this.opponent);
      Iterator var3 = players.iterator();

      while(var3.hasNext()) {
         PlayerCommonState player = (PlayerCommonState)var3.next();
         Iterator var5 = player.getActiveAndBenchCards().iterator();

         while(var5.hasNext()) {
            PokemonCardState pokemon = (PokemonCardState)var5.next();
            if (pokemon.getAbility() != null && pokemon.getAbility().getEffect() != null && pokemon.getAbility().getEffect().disableEvolution(affecting, pokemon, this)) {
               return true;
            }

            if (pokemon.getHiddenAbility() != null && pokemon.getHiddenAbility().getEffect() != null && pokemon.getHiddenAbility().getEffect().disableEvolution(affecting, pokemon, this)) {
               return true;
            }
         }
      }

      return false;
   }
}

package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.duel.dto.CustomGUI;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseAbilityEffect {
   private final String code;

   public BaseAbilityEffect(String code) {
      this.code = code;
   }

   public boolean isPassive() {
      return false;
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      List allCards = new ArrayList();
      allCards.addAll(client.getMe().getActiveAndBenchCards());
      allCards.addAll(client.getOpponent().getActiveAndBenchCards());
      Iterator var4 = allCards.iterator();

      while(var4.hasNext()) {
         PokemonCardState card = (PokemonCardState)var4.next();
         if (card != pokemon && card.getAbility() != null && !pokemon.getStatus().hasCondition(CardCondition.ASLEEP) && !pokemon.getStatus().hasCondition(CardCondition.CONFUSED) && !pokemon.getStatus().hasCondition(CardCondition.PARALYZED) && card.getAbility().getEffect().disableOtherAbilities(pokemon, client)) {
            return false;
         }
      }

      return !pokemon.getStatus().hasCondition(CardCondition.ASLEEP) && !pokemon.getStatus().hasCondition(CardCondition.CONFUSED) && !pokemon.getStatus().hasCondition(CardCondition.PARALYZED) && !pokemon.abilitiesDisabled();
   }

   public int onDamage(PokemonCardState active, PokemonCardState attacker, GameServerState server, int damage) {
      return damage;
   }

   public int onAttacked(PokemonCardState active, PokemonCardState attacker, GameServerState server) {
      return -1;
   }

   public boolean onCondition(PokemonCardState pokemon, PokemonCardState attacker, CardCondition cardCondition, GameServerState server) {
      return true;
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState server) {
      return false;
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      return null;
   }

   public CustomGUI getCustomGUI(PokemonCardState pokemon, GameServerState server) {
      return null;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
   }

   public void cleanUp(PokemonCardState pokemon, GameServerState server) {
      pokemon.parameters.clear();
   }

   public void onPlay(PokemonCardState newPokemon, PlayerCommonState playingPlayer, PokemonCardState pokemon, PlayerServerState player, GameServerState server) {
   }

   public void onStartGame(PokemonCardState pokemon, GameServerState server) {
   }

   public void onEndTurn(PokemonCardState pokemon, GameServerState server) {
   }

   public void onSwitchActiveCard(PokemonCardState newActive, PokemonCardState oldActive, PlayerCommonState switchingPlayer, PokemonCardState pokemon, PlayerServerState player, GameServerState server) {
   }

   public List getEnergyEquivalence(CommonCardState attachment) {
      return null;
   }

   public List flipCoin() {
      return new ArrayList();
   }

   public int retreatModifier(PokemonCardState pokemon, PlayerCommonState player) {
      return 0;
   }

   public boolean disableEvolution(PokemonCardState pokemon, GameClientState client) {
      return false;
   }

   public boolean disableEvolution(PokemonCardState affecting, PokemonCardState pokemon, GameClientState client) {
      return false;
   }

   public boolean disableOtherAbilities(PokemonCardState pokemon, GameClientState server) {
      return false;
   }

   public boolean holdParameters() {
      return false;
   }

   public boolean revealHand() {
      return false;
   }

   public boolean ignoreEnergyTypes() {
      return false;
   }

   public String getCode() {
      return this.code;
   }
}

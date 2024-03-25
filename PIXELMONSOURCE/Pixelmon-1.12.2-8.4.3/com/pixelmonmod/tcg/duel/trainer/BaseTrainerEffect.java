package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.List;

public class BaseTrainerEffect {
   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return true;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
   }

   public boolean canSkipSelector() {
      return true;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }

   public boolean canPlay(GameClientState client) {
      return false;
   }

   public final boolean canPlay(GameServerState server) {
      return this.canPlay(new GameClientState(server));
   }

   public boolean canPlaceOn(CardWithLocation card) {
      return false;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      return null;
   }

   public CardSelectorState getOpponentRevealingSelectorState(TrainerCardState trainer, GameServerState server) {
      return null;
   }

   public List flipCoin() {
      return new ArrayList();
   }

   public int modifyDamage(int damage, TrainerCardState trainer, GameServerState server) {
      return damage;
   }

   public boolean preventDiscard() {
      return false;
   }

   public void handleEndTurn(TrainerCardState trainer, PokemonCardState card, PlayerServerState player, GameServerState server) {
   }

   public boolean showDiscardButton() {
      return false;
   }
}

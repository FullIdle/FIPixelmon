package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class FullHeal extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return server.getPlayer(server.getCurrentTurn()).getActiveCard() != null;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PokemonCardState card = server.getPlayer(server.getCurrentTurn()).getActiveCard();
      card.getStatus().getConditions().clear();
   }

   public boolean canPlaceOn(CardWithLocation card) {
      return card != null && card.getCard() != null && card.isMine() && card.getLocation() == BoardLocation.Active;
   }

   public boolean canSkipSelector() {
      return true;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
      pokemon.getStatus().getConditions().clear();
   }
}

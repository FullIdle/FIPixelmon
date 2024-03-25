package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class Potion extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public boolean canPlaceOn(CardWithLocation card) {
      return card != null && card.getCard() != null && card.isMine() && (card.getLocation() == BoardLocation.Active || card.getLocation() == BoardLocation.Bench);
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      return SelectorHelper.generateSelectorForActiveAndBench(server.getPlayer(server.getCurrentTurn()), "card.base94.effect.selector");
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 1;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PokemonCardState pokemon = (PokemonCardState)trainer.getParameters().get(0);
      pokemon.getStatus().setDamage(pokemon.getStatus().getDamage() - 20);
   }

   public boolean canSkipSelector() {
      return true;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
      pokemon.getStatus().setDamage(pokemon.getStatus().getDamage() - 20);
   }
}

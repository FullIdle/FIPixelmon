package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerClientMyState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class PokemonTrader extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      PlayerClientMyState me = client.getMe();
      return me.getHand().stream().anyMatch((c) -> {
         return c.getCardType().isPokemon();
      });
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      if (trainer.getParameters().size() == 0) {
         return SelectorHelper.generateSelectorForSelectPokemonFromHand(me, 1, (String)null);
      } else {
         return trainer.getParameters().size() == 1 ? SelectorHelper.generateSelectorForSelectPokemonFromDeck(me, 1, (String)null) : null;
      }
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      if (!server.getPlayer(server.getCurrentTurn()).getDeck().stream().anyMatch((c) -> {
         return c.getCardType().isPokemon();
      })) {
         return true;
      } else {
         return trainer.getParameters().size() == 2;
      }
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      if (trainer.getParameters().size() == 2) {
         CommonCardState handCard = (CommonCardState)trainer.getParameters().get(0);
         CommonCardState deckCard = (CommonCardState)trainer.getParameters().get(1);
         PlayerServerState me = server.getPlayer(server.getCurrentTurn());
         me.getHand().remove(handCard.getData());
         me.getDeck().add(handCard.getData());
         me.getDeck().remove(deckCard.getData());
         me.getHand().add(deckCard.getData());
         LogicHelper.shuffleCardList(me.getDeck());
         CardSelectorState selector = new CardSelectorState(0, 0, CardSelectorDisplay.Reveal, false, "trainer.pokemontrader.selector.reveal");
         selector.getCardList().add(new CardWithLocation(handCard, true, BoardLocation.Hand, 0));
         selector.getCardList().add(new CardWithLocation(deckCard, true, BoardLocation.Deck, 0));
         server.getPlayer(server.getNextTurn()).setCardSelectorState(selector);
      }
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

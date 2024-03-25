package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class EnergySearch extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      return SelectorHelper.generateSelectorForSelectEnergyFromDeck(me, 1, (String)null);
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      if (!server.getPlayer(server.getCurrentTurn()).getDeck().stream().anyMatch((c) -> {
         return c.getCardType() == CardType.ENERGY;
      })) {
         return true;
      } else {
         return trainer.getParameters().size() == 1;
      }
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      if (trainer.getParameters().size() == 1) {
         CommonCardState toDraw = (CommonCardState)trainer.getParameters().get(0);
         PlayerServerState me = server.getPlayer(server.getCurrentTurn());
         me.getDeck().remove(toDraw.getData());
         me.getHand().add(toDraw.getData());
         LogicHelper.shuffleCardList(me.getDeck());
         CardSelectorState selector = new CardSelectorState(0, 0, CardSelectorDisplay.Reveal, false, "trainer.energysearch.selector.reveal");
         selector.getCardList().add(new CardWithLocation(toDraw, true, BoardLocation.Deck, 0));
         server.getPlayer(server.getNextTurn()).setCardSelectorState(selector);
      }
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

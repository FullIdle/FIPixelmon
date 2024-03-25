package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.Iterator;

public class EvolutionaryLight extends BaseAbilityEffect {
   public EvolutionaryLight() {
      super("EvolutionaryLight");
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return this.findEvolutionCard(client) != null && pokemon.getParameters().size() == 1;
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, true);
      PlayerServerState currentPlayer = server.getCurrentPlayer();

      for(int i = 0; i < currentPlayer.getDeck().size(); ++i) {
         ImmutableCard immutableCard = (ImmutableCard)currentPlayer.getDeck().get(i);
         if (immutableCard.isEvolution()) {
            selector.addCard(new CardWithLocation(new CommonCardState(immutableCard), true, BoardLocation.Deck, i));
         }
      }

      return selector;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      if (pokemon.getParameters().size() == 1) {
         CommonCardState card = (CommonCardState)pokemon.getParameters().get(0);
         PlayerServerState currentPlayer = server.getPlayer(server.getCurrentTurn());
         currentPlayer.getDeck().remove(card.getData());
         currentPlayer.getHand().add(card.getData());
         LogicHelper.shuffleCardList(server.getCurrentPlayer().getDeck());
         CardSelectorState selector = new CardSelectorState(0, 0, CardSelectorDisplay.Reveal, false, "trainer.evolutionarylight.selector.reveal");
         selector.getCardList().add(new CardWithLocation(card, true, BoardLocation.Deck, 0));
         server.getPlayer(server.getNextTurn()).setCardSelectorState(selector);
      }
   }

   private ImmutableCard findEvolutionCard(GameServerState client) {
      Iterator var2 = client.getCurrentPlayer().getDeck().iterator();

      ImmutableCard immutableCard;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         immutableCard = (ImmutableCard)var2.next();
      } while(!immutableCard.isEvolution());

      return immutableCard;
   }
}

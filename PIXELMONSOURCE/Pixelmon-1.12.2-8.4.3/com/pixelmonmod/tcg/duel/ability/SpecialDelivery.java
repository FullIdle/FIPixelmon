package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class SpecialDelivery extends BaseAbilityEffect {
   public SpecialDelivery() {
      super("SpecialDelivery");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      if (!super.isEnabled(pokemon, client)) {
         return false;
      } else if (client.getMe().getDeckSize() < 1) {
         return false;
      } else {
         return pokemon.getParameters().isEmpty();
      }
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      PlayerServerState player = server.getPlayer(server.getCurrentTurn());
      if (!pokemon.getParameters().isEmpty()) {
         return null;
      } else {
         player.drawCards(1, server);
         CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Discard, false, "ability.specialdelivery.selector.discard");

         for(int i = 0; i < player.getHand().size(); ++i) {
            ImmutableCard card = (ImmutableCard)player.getHand().get(i);
            selector.getCardList().add(new CardWithLocation(new CommonCardState(card), true, BoardLocation.Hand, i));
         }

         return selector;
      }
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return pokemon.getParameters().size() == 1;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      CommonCardState placeOnDeck = (CommonCardState)pokemon.getParameters().get(0);
      player.getDeck().add(0, placeOnDeck.getData());
      player.getHand().remove(placeOnDeck.getData());
   }

   public void cleanUp(PokemonCardState pokemon, GameServerState server) {
   }
}

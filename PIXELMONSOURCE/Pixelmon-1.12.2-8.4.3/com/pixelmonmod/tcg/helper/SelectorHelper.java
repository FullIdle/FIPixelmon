package com.pixelmonmod.tcg.helper;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class SelectorHelper {
   public static CardSelectorState generateSelectorForActiveAndBench(PlayerCommonState me, String customText) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, customText);
      if (me.getActiveCard() != null) {
         selector.getCardList().add(new CardWithLocation(me.getActiveCard(), true, BoardLocation.Active, 0));
      }

      for(int i = 0; i < me.getBenchCards().length; ++i) {
         if (me.getBenchCards()[i] != null) {
            selector.getCardList().add(new CardWithLocation(me.getBenchCards()[i], true, BoardLocation.Bench, i));
         }
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForBench(PlayerCommonState me, String customText) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, customText);

      for(int i = 0; i < me.getBenchCards().length; ++i) {
         if (me.getBenchCards()[i] != null) {
            selector.getCardList().add(new CardWithLocation(me.getBenchCards()[i], true, BoardLocation.Bench, i));
         }
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForDiscardPile(PlayerCommonState me, String customText) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, customText);

      for(int i = 0; i < me.getBenchCards().length; ++i) {
         if (me.getBenchCards()[i] != null) {
            selector.getCardList().add(new CardWithLocation(me.getBenchCards()[i], true, BoardLocation.DiscardPile, 0));
         }
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForDiscardFromHand(PlayerServerState me, int count, String customText) {
      CardSelectorState selector = new CardSelectorState(count, count, CardSelectorDisplay.Discard, false, customText);

      for(int i = 0; i < me.getHand().size(); ++i) {
         selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getHand().get(i)), true, BoardLocation.Hand, i));
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForSelectPokemonFromHand(PlayerServerState me, int count, String customText) {
      CardSelectorState selector = new CardSelectorState(count, count, CardSelectorDisplay.Select, false, customText);

      for(int i = 0; i < me.getHand().size(); ++i) {
         if (((ImmutableCard)me.getHand().get(i)).getCardType().isPokemon()) {
            selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getHand().get(i)), true, BoardLocation.Hand, i));
         }
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForSelectPokemonFromDeck(PlayerServerState me, int count, String customText) {
      CardSelectorState selector = new CardSelectorState(count, count, CardSelectorDisplay.Select, false, customText);

      for(int i = 0; i < me.getDeck().size(); ++i) {
         if (((ImmutableCard)me.getDeck().get(i)).getCardType().isPokemon()) {
            selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getDeck().get(i)), true, BoardLocation.Deck, i));
         }
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForSelectEnergyFromDeck(PlayerServerState me, int count, String customText) {
      CardSelectorState selector = new CardSelectorState(count, count, CardSelectorDisplay.Select, false, customText);

      for(int i = 0; i < me.getDeck().size(); ++i) {
         if (((ImmutableCard)me.getDeck().get(i)).getCardType() == CardType.ENERGY) {
            selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getDeck().get(i)), true, BoardLocation.Deck, i));
         }
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForPokemonWithEnergy(PlayerServerState player, boolean isMe, String customText) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, customText);
      if (player.getActiveCard() != null && player.getActiveCard().getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
         selector.getCardList().add(new CardWithLocation(player.getActiveCard(), isMe, BoardLocation.Active, 0));
      }

      for(int i = 0; i < player.getBenchCards().length; ++i) {
         if (player.getBenchCards()[i] != null && player.getBenchCards()[i].getAttachments().stream().anyMatch(CommonCardState::isEnergyEquivalence)) {
            selector.getCardList().add(new CardWithLocation(player.getBenchCards()[i], isMe, BoardLocation.Bench, i));
         }
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForRevealingHand(PlayerServerState me, String customText) {
      CardSelectorState selector = new CardSelectorState(0, 0, CardSelectorDisplay.Reveal, false, customText);

      for(int i = 0; i < me.getHand().size(); ++i) {
         selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getHand().get(i)), true, BoardLocation.Hand, i));
      }

      return selector;
   }

   public static CardSelectorState generateSelectorForPlayer(PlayerServerState me, PlayerServerState opp, String customText) {
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false, customText);
      selector.getCardList().add(new CardWithLocation(me.getActiveCard(), true, BoardLocation.Active, 0));
      selector.getCardList().add(new CardWithLocation(opp.getActiveCard(), false, BoardLocation.Active, 0));
      return selector;
   }
}

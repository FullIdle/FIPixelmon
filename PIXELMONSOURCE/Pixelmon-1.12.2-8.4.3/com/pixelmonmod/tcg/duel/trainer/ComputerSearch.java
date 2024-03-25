package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.ImmutableCard;
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

public class ComputerSearch extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return client.getMe().getHand().size() >= 3 && client.getMe().getDeckSize() > 0;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      if (trainer.getParameters().size() == 0) {
         return SelectorHelper.generateSelectorForDiscardFromHand(me, 2, (String)null);
      } else if (trainer.getParameters().size() != 2) {
         return null;
      } else {
         CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false);

         for(int i = 0; i < me.getDeck().size(); ++i) {
            selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getDeck().get(i)), true, BoardLocation.Deck, i));
         }

         return selector;
      }
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 3;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      CommonCardState handCard1 = (CommonCardState)trainer.getParameters().get(0);
      CommonCardState handCard2 = (CommonCardState)trainer.getParameters().get(1);
      CommonCardState deckCard = (CommonCardState)trainer.getParameters().get(2);
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      me.getHand().remove(handCard1.getData());
      me.getDiscardPile().add(handCard1.getData());
      me.getHand().remove(handCard2.getData());
      me.getDiscardPile().add(handCard2.getData());
      me.getHand().add(deckCard.getData());
      me.getDeck().remove(deckCard.getData());
      LogicHelper.shuffleCardList(me.getDeck());
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

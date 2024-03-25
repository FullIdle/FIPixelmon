package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.CardType;
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
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class EnergyRetrieval extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return client.getMe().getHand().size() >= 2 && client.getMe().getDiscardPile().stream().anyMatch((c) -> {
         return c.getCardType() == CardType.ENERGY;
      });
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      if (trainer.getParameters().size() == 0) {
         return SelectorHelper.generateSelectorForDiscardFromHand(me, 1, (String)null);
      } else if (trainer.getParameters().size() == 1) {
         CardSelectorState selector = new CardSelectorState(1, 2, CardSelectorDisplay.Select, false);

         for(int i = 0; i < me.getDiscardPile().size(); ++i) {
            if (((ImmutableCard)me.getDiscardPile().get(i)).getCardType() == CardType.ENERGY) {
               selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getDiscardPile().get(i)), true, BoardLocation.DiscardPile, i));
            }
         }

         return selector;
      } else {
         return null;
      }
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() >= 2;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      CommonCardState handCard = (CommonCardState)trainer.getParameters().get(0);
      CommonCardState energy1 = (CommonCardState)trainer.getParameters().get(1);
      CommonCardState energy2 = null;
      if (trainer.getParameters().size() > 2) {
         energy2 = (CommonCardState)trainer.getParameters().get(2);
      }

      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      me.getHand().remove(handCard.getData());
      me.getDiscardPile().add(handCard.getData());
      me.getHand().add(energy1.getData());
      me.getDiscardPile().remove(energy1.getData());
      if (energy2 != null) {
         me.getHand().add(energy2.getData());
         me.getDiscardPile().remove(energy2.getData());
      }

   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.List;

public class Recycle extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return client.getMe().getDeckSize() > 0;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      CardSelectorState selector = new CardSelectorState(1, 1, CardSelectorDisplay.Select, false);

      for(int i = 0; i < me.getDiscardPile().size(); ++i) {
         if (((ImmutableCard)me.getDiscardPile().get(i)).getCardType() == CardType.TRAINER) {
            selector.getCardList().add(new CardWithLocation(new CommonCardState((ImmutableCard)me.getDiscardPile().get(i)), true, BoardLocation.DiscardPile, i));
         }
      }

      return selector;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      if (server.getPlayer(server.getCurrentTurn()).getDeck().isEmpty()) {
         return true;
      } else {
         return trainer.getParameters().size() == 1;
      }
   }

   public List flipCoin() {
      List coin = new ArrayList();
      coin.add(CoinSide.getRandom());
      return coin;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      if (trainer.getParameters().size() == 1) {
         PlayerServerState me = server.getPlayer(server.getCurrentTurn());
         ImmutableCard card = ((CommonCardState)trainer.getParameters().get(0)).getData();
         if (this.isCorrectCoinSide(server)) {
            me.getDeck().add(0, card);
            me.getDiscardPile().remove(card);
         }

      }
   }

   private boolean isCorrectCoinSide(GameServerState server) {
      return server.getCoinFlip() != null && server.getCoinFlip().getResults().get(0) == CoinSide.Head;
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

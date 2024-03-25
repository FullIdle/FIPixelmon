package com.pixelmonmod.tcg.duel.trainer;

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
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;
import java.util.ArrayList;
import java.util.List;

public class PokeBall extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      if (server.getCoinFlip().getResults().get(0) == CoinSide.Head) {
         PlayerServerState me = server.getPlayer(server.getCurrentTurn());
         return SelectorHelper.generateSelectorForSelectPokemonFromDeck(me, 1, (String)null);
      } else {
         return null;
      }
   }

   public CardSelectorState getOpponentRevealingSelectorState(TrainerCardState trainer, GameServerState server) {
      if (server.getCoinFlip().getResults().get(0) == CoinSide.Head && trainer.getParameters().size() > 0) {
         PlayerServerState me = server.getPlayer(server.getCurrentTurn());
         CardSelectorState selector = new CardSelectorState(0, 0, CardSelectorDisplay.Reveal, false);
         selector.getCardList().add(new CardWithLocation((CommonCardState)trainer.getParameters().get(0), true, BoardLocation.Deck, 0));
         return selector;
      } else {
         return null;
      }
   }

   public List flipCoin() {
      List result = new ArrayList();
      result.add(CoinSide.getRandom());
      return result;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return server.getCoinFlip().getResults().get(0) == CoinSide.Tail || trainer.getParameters().size() == 1;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      if (server.getCoinFlip().getResults().get(0) == CoinSide.Head) {
         ImmutableCard card = ((CommonCardState)trainer.getParameters().get(0)).getData();
         PlayerServerState me = server.getPlayer(server.getCurrentTurn());
         me.getDeck().remove(card);
         me.getHand().add(card);
         LogicHelper.shuffleCardList(me.getDeck());
      }

   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

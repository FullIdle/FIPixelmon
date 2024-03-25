package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class Lass extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return true;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      return SelectorHelper.generateSelectorForRevealingHand(server.getPlayer(server.getNextTurn()), (String)null);
   }

   public CardSelectorState getOpponentRevealingSelectorState(TrainerCardState trainer, GameServerState server) {
      return SelectorHelper.generateSelectorForRevealingHand(server.getPlayer(server.getCurrentTurn()), (String)null);
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      return me.getCardSelectorResult() != null && me.getCardSelectorResult().isOpened();
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      PlayerServerState opp = server.getPlayer(server.getNextTurn());
      this.shuffleTrainersIntoDeck(me);
      this.shuffleTrainersIntoDeck(opp);
   }

   private void shuffleTrainersIntoDeck(PlayerServerState player) {
      int i = 0;

      while(i < player.getHand().size()) {
         if (((ImmutableCard)player.getHand().get(i)).getCardType() == CardType.TRAINER) {
            player.getDeck().add(player.getHand().get(i));
            player.getHand().remove(i);
         } else {
            ++i;
         }
      }

      LogicHelper.shuffleCardList(player.getDeck());
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

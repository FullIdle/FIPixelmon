package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.CommonCardState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.helper.SelectorHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class Maintenance extends BaseTrainerEffect {
   public boolean canPlay(GameClientState client) {
      return client.getMe().getHand().size() >= 3;
   }

   public CardSelectorState getSelectorState(TrainerCardState trainer, GameServerState server) {
      return SelectorHelper.generateSelectorForDiscardFromHand(server.getPlayer(server.getCurrentTurn()), 2, "trainer.maintenance.selector.choose");
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return trainer.getParameters().size() == 2;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      CommonCardState card1 = (CommonCardState)trainer.getParameters().get(0);
      CommonCardState card2 = (CommonCardState)trainer.getParameters().get(1);
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      me.getHand().remove(card1.getData());
      me.getHand().remove(card2.getData());
      me.getDeck().add(card1.getData());
      me.getDeck().add(card2.getData());
      LogicHelper.shuffleCardList(me.getDeck());
      me.drawCards(1, server);
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

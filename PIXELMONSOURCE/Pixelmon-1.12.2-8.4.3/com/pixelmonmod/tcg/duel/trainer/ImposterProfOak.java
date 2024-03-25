package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class ImposterProfOak extends BaseTrainerEffect {
   public boolean canPlay(GameClientState server) {
      return true;
   }

   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return true;
   }

   public void apply(TrainerCardState card, GameServerState server) {
      PlayerServerState opp = server.getPlayer(server.getNextTurn());

      while(!opp.getHand().isEmpty()) {
         opp.getDeck().add(opp.getHand().get(0));
         opp.getHand().remove(0);
      }

      LogicHelper.shuffleCardList(opp.getDeck());
      opp.drawCards(7, server);
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

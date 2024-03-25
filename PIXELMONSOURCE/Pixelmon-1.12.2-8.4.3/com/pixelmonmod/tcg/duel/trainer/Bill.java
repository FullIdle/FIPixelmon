package com.pixelmonmod.tcg.duel.trainer;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.duel.state.TrainerCardState;
import com.pixelmonmod.tcg.network.packets.enums.BoardLocation;

public class Bill extends BaseTrainerEffect {
   public boolean canApply(TrainerCardState trainer, GameServerState server) {
      return true;
   }

   public void apply(TrainerCardState trainer, GameServerState server) {
      PlayerServerState me = server.getPlayer(server.getCurrentTurn());
      me.drawCards(2, server);
   }

   public boolean canPlay(GameClientState client) {
      return true;
   }

   public boolean canSkipSelector() {
      return false;
   }

   public void applySkipSelector(TrainerCardState trainer, PokemonCardState pokemon, GameServerState server, BoardLocation b, int p) {
   }
}

package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class StepIn extends BaseAbilityEffect {
   public StepIn() {
      super("StepIn");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      return super.isEnabled(pokemon, client) && client.getMe().isInBench(pokemon);
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
      player.switchActive(pokemon, server);
   }
}

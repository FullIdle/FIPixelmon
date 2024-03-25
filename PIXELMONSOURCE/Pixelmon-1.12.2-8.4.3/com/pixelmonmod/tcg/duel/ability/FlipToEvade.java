package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.attack.enums.CoinSide;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class FlipToEvade extends BaseAbilityEffect {
   public FlipToEvade() {
      super("FlipToEvade");
   }

   public boolean isPassive() {
      return true;
   }

   public int onAttacked(PokemonCardState active, PokemonCardState attacker, GameServerState server) {
      return CoinSide.getRandom() == CoinSide.Head && this.isEnabled(active, new GameClientState(server)) ? 0 : -1;
   }
}

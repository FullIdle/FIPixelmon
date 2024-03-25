package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class DisableEvolution extends BaseAbilityEffect {
   public DisableEvolution() {
      super("DisableEvolution");
   }

   public boolean isPassive() {
      return true;
   }

   public boolean disableEvolution(PokemonCardState affecting, PokemonCardState pokemon, GameClientState client) {
      return this.isEnabled(pokemon, client);
   }

   public boolean disableEvolution(PokemonCardState pokemon, GameClientState client) {
      return this.isEnabled(pokemon, client);
   }
}

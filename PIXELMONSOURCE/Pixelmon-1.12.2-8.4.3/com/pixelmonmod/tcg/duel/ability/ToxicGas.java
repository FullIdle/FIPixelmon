package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class ToxicGas extends BaseAbilityEffect {
   public ToxicGas() {
      super("ToxicGas");
   }

   public boolean isPassive() {
      return true;
   }

   public boolean disableOtherAbilities(PokemonCardState pokemon, GameClientState client) {
      return this.isEnabled(pokemon, client);
   }
}

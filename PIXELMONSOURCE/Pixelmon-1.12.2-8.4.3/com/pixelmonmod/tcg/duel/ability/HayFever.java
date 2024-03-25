package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.CardSelectorState;
import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;

public class HayFever extends BaseAbilityEffect {
   public HayFever() {
      super("HayFever");
   }

   public boolean isEnabled(PokemonCardState pokemon, GameClientState client) {
      return false;
   }

   public CardSelectorState getSelectorState(PokemonCardState pokemon, GameServerState server) {
      return new CardSelectorState(0, 0, (CardSelectorDisplay)null, false);
   }

   public boolean canActivate(PokemonCardState pokemon, GameServerState client) {
      return false;
   }

   public void activate(PokemonCardState pokemon, GameServerState server, PlayerServerState player) {
   }
}

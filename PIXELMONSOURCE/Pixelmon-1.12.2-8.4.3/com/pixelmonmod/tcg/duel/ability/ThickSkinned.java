package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.api.card.CardCondition;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class ThickSkinned extends BaseAbilityEffect {
   public ThickSkinned() {
      super("ThickSkinned");
   }

   public boolean isPassive() {
      return true;
   }

   public boolean onCondition(PokemonCardState pokemon, PokemonCardState attacker, CardCondition cardCondition, GameServerState server) {
      return false;
   }
}

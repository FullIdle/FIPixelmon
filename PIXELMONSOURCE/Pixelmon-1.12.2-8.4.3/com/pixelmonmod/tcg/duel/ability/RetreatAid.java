package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.PlayerCommonState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class RetreatAid extends BaseAbilityEffect {
   public RetreatAid() {
      super("RetreatAid");
   }

   public boolean isPassive() {
      return true;
   }

   public int retreatModifier(PokemonCardState pokemon, PlayerCommonState player) {
      PokemonCardState[] var3 = player.getBenchCards();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PokemonCardState card = var3[var5];
         if (card == pokemon) {
            return -1;
         }
      }

      return 0;
   }
}

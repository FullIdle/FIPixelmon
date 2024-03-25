package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class StrikesBack extends BaseAbilityEffect {
   private final int backDamage;

   public StrikesBack(String code, int damage) {
      super(code);
      this.backDamage = damage;
   }

   public boolean isPassive() {
      return true;
   }

   public int onDamage(PokemonCardState active, PokemonCardState attacker, GameServerState server, int damage) {
      if (damage > 0 && attacker != null && this.isEnabled(active, new GameClientState(server)) && active != attacker) {
         attacker.addDamage((PokemonCardState)null, this.backDamage, server);
      }

      return damage;
   }
}

package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class HalfDamage extends BaseAbilityEffect {
   public HalfDamage() {
      super("HalfDamage");
   }

   public boolean isPassive() {
      return true;
   }

   public int onDamage(PokemonCardState active, PokemonCardState attacker, GameServerState server, int damage) {
      int calc = damage / 2;
      calc -= calc % 10;
      return calc;
   }
}

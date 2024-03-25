package com.pixelmonmod.tcg.duel.ability;

import com.pixelmonmod.tcg.duel.state.GameClientState;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class InvisibleWall extends BaseAbilityEffect {
   private final int amount;

   public InvisibleWall(String code, int amount) {
      super(code);
      this.amount = amount;
   }

   public boolean isPassive() {
      return true;
   }

   public int onDamage(PokemonCardState active, PokemonCardState attacker, GameServerState server, int damage) {
      return damage > this.amount && this.isEnabled(active, new GameClientState(server)) ? 0 : damage;
   }
}

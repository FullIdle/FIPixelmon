package com.pixelmonmod.tcg.duel.attack;

import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.duel.state.PokemonAttackStatus;
import com.pixelmonmod.tcg.duel.state.PokemonCardState;

public class PendingAttack {
   private final PlayerServerState player;
   private final PokemonCardState pokemon;
   private final PokemonAttackStatus attack;

   public PendingAttack(PlayerServerState player, PokemonCardState pokemon, PokemonAttackStatus attack) {
      attack.resetDamage();
      this.player = player;
      this.pokemon = pokemon;
      this.attack = attack;
   }

   public PlayerServerState getPlayer() {
      return this.player;
   }

   public PokemonCardState getPokemon() {
      return this.pokemon;
   }

   public PokemonAttackStatus getAttack() {
      return this.attack;
   }
}

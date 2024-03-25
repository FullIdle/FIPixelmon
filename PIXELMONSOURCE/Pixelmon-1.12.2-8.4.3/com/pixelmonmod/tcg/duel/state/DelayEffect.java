package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.tcg.duel.attack.effects.BaseAttackEffect;

public class DelayEffect {
   private BaseAttackEffect effect;
   private PokemonAttackStatus attack;
   private PokemonCardState pokemon;
   private int turn;

   public DelayEffect(BaseAttackEffect effect, PokemonAttackStatus attack, PokemonCardState pokemon, int turn) {
      this.effect = effect;
      this.attack = attack;
      this.pokemon = pokemon;
      this.turn = turn;
   }

   public BaseAttackEffect getEffect() {
      return this.effect;
   }

   public PokemonAttackStatus getAttack() {
      return this.attack;
   }

   public PokemonCardState getPokemon() {
      return this.pokemon;
   }

   public int getTurn() {
      return this.turn;
   }
}

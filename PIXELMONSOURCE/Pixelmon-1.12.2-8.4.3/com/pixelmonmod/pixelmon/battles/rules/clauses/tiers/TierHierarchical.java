package com.pixelmonmod.pixelmon.battles.rules.clauses.tiers;

import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import java.util.Set;
import java.util.function.Predicate;

public class TierHierarchical extends TierSet {
   private TierHierarchical tierAbove;

   public TierHierarchical(String id, Set pokemon, TierHierarchical tierAbove) {
      super(id, pokemon);
      this.tierAbove = tierAbove;
   }

   protected Predicate getCondition() {
      return (p) -> {
         return !this.isBanned(p);
      };
   }

   private boolean isBanned(PokemonForm pokemonForm) {
      return this.tierAbove != null && (this.tierAbove.isInSet(pokemonForm) || this.tierAbove.isBanned(pokemonForm));
   }
}

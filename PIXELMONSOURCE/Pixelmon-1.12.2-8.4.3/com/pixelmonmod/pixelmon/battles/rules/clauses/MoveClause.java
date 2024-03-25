package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;

public class MoveClause extends BattleClause {
   private String[] moves;

   public MoveClause(String id, String... moves) {
      super(id);
      this.moves = moves;
      ArrayHelper.validateArrayNonNull(moves);
   }

   public boolean validateSingle(Pokemon pokemon) {
      return !pokemon.getMoveset().hasAttack(this.moves);
   }
}

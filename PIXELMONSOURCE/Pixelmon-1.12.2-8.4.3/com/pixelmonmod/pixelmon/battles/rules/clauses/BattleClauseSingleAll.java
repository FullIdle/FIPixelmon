package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Iterator;
import java.util.List;

public class BattleClauseSingleAll extends BattleClauseAll {
   public BattleClauseSingleAll(String id, BattleClause... clauses) {
      super(id, clauses);
   }

   public boolean validateTeam(List team) {
      Iterator var2 = team.iterator();

      Pokemon pokemon;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         pokemon = (Pokemon)var2.next();
      } while(this.validateSingle(pokemon));

      return false;
   }
}

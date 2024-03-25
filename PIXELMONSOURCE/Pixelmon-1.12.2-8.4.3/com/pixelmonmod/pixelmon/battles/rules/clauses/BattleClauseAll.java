package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import java.util.List;

public class BattleClauseAll extends BattleClause {
   private BattleClause[] clauses;

   public BattleClauseAll(String id, BattleClause... clauses) {
      super(id);
      this.clauses = clauses;
      ArrayHelper.validateArrayNonNull(clauses);
   }

   public boolean validateSingle(Pokemon pokemon) {
      BattleClause[] var2 = this.clauses;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BattleClause clause = var2[var4];
         if (clause.validateSingle(pokemon)) {
            return true;
         }
      }

      return false;
   }

   public boolean validateTeam(List team) {
      BattleClause[] var2 = this.clauses;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BattleClause clause = var2[var4];
         if (clause.validateTeam(team)) {
            return true;
         }
      }

      return false;
   }
}

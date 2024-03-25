package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import java.util.List;

public class BattleClauseAny extends BattleClause {
   private BattleClause[] clauses;

   public BattleClauseAny(String id, BattleClause... clauses) {
      super(id);
      this.clauses = clauses;
      ArrayHelper.validateArrayNonNull(clauses);
   }

   public boolean validateSingle(Pokemon pokemon) {
      BattleClause[] var2 = this.clauses;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BattleClause clause = var2[var4];
         if (!clause.validateSingle(pokemon)) {
            return false;
         }
      }

      return true;
   }

   public boolean validateTeam(List team) {
      BattleClause[] var2 = this.clauses;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BattleClause clause = var2[var4];
         if (!clause.validateTeam(team)) {
            return false;
         }
      }

      return true;
   }
}

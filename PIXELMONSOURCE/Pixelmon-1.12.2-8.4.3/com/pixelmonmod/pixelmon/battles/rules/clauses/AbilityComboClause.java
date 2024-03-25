package com.pixelmonmod.pixelmon.battles.rules.clauses;

public class AbilityComboClause extends BattleClauseAll {
   public AbilityComboClause(String id, Class... abilities) {
      super(id, getClauses(abilities));
   }

   private static AbilityClause[] getClauses(Class... abilities) {
      AbilityClause[] abilityClauses = new AbilityClause[abilities.length];

      for(int i = 0; i < abilities.length; ++i) {
         abilityClauses[i] = new AbilityClause("", new Class[]{abilities[i]});
      }

      return abilityClauses;
   }
}

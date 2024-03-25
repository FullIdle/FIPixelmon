package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import java.util.Iterator;
import java.util.List;

class BatonPass1Clause extends BattleClause {
   private MoveClause batonPassCheck = new MoveClause("", new String[]{"Baton Pass"});

   BatonPass1Clause() {
      super("batonpass1");
   }

   public boolean validateTeam(List team) {
      boolean hasOneBatonPass = false;
      Iterator var3 = team.iterator();

      while(var3.hasNext()) {
         Pokemon pokemon = (Pokemon)var3.next();
         if (!this.batonPassCheck.validateSingle(pokemon)) {
            if (hasOneBatonPass) {
               return false;
            }

            hasOneBatonPass = true;
         }
      }

      return true;
   }
}

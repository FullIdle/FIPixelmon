package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class SpeciesClause extends BattleClause {
   SpeciesClause() {
      super("pokemon");
   }

   public boolean validateTeam(List team) {
      Set species = new HashSet();
      Iterator var3 = team.iterator();

      while(var3.hasNext()) {
         Pokemon pokemon = (Pokemon)var3.next();
         EnumSpecies current = pokemon.getBaseStats().getSpecies();
         if (species.contains(current)) {
            return false;
         }

         species.add(current);
      }

      return true;
   }
}

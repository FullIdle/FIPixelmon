package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;

public class PokemonClause extends BattleClause {
   private PokemonForm[] pokemon;
   private boolean validateForm;

   public PokemonClause(String id, EnumSpecies... pokemon) {
      this(id, PokemonForm.convertEnumArray(pokemon));
      this.validateForm = false;
   }

   public PokemonClause(String id, PokemonForm... pokemon) {
      super(id);
      this.validateForm = true;
      ArrayHelper.validateArrayNonNull(pokemon);
      this.pokemon = pokemon;
   }

   public boolean validateSingle(Pokemon pokemon) {
      PokemonForm[] var2 = this.pokemon;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PokemonForm pokemonForm = var2[var4];
         if (pokemonForm.pokemon == pokemon.getBaseStats().getSpecies() && (!this.validateForm || pokemonForm.form == pokemon.getForm())) {
            return false;
         }
      }

      return true;
   }
}

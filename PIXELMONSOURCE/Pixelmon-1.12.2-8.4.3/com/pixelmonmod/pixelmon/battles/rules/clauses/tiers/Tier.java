package com.pixelmonmod.pixelmon.battles.rules.clauses.tiers;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Tier extends BattleClause {
   protected Predicate condition;
   private static final Set ignoreForms;

   public Tier(String id) {
      this(id, (p) -> {
         return true;
      });
   }

   public Tier(String id, Predicate condition) {
      super(id);
      this.condition = condition;
   }

   public final boolean validateSingle(Pokemon pokemon) {
      EnumSpecies species = pokemon.getSpecies();
      PokemonForm pokemonForm = new PokemonForm(species, pokemon.getForm(), pokemon.getGender());
      if (pokemonForm.form != -1 && (ignoreForms.contains(species) || !Entity3HasStats.hasForms(species))) {
         pokemonForm.form = -1;
      }

      ItemHeld heldItem = pokemon.getHeldItemAsItemHeld();
      if (PixelmonWrapper.canMegaEvolve(heldItem, species, pokemonForm.form)) {
         if (heldItem instanceof ItemMegaStone) {
            pokemonForm.form = ((ItemMegaStone)heldItem).getForm(pokemonForm.form);
         } else if (species == EnumSpecies.Necrozma) {
            pokemonForm.form = EnumNecrozma.ULTRA.getForm();
         }
      }

      return this.condition.test(pokemonForm);
   }

   public final boolean validateTeam(List team) {
      return super.validateTeam(team);
   }

   public String getTierDescription() {
      return this.getLocalizedName();
   }

   static {
      ignoreForms = new HashSet(Arrays.asList(EnumSpecies.Burmy, EnumSpecies.Castform, EnumSpecies.Gastrodon, EnumSpecies.Shellos, EnumSpecies.Unown));
   }
}

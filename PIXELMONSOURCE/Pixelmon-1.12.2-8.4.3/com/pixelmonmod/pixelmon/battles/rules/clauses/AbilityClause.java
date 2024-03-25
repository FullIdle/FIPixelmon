package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;

public class AbilityClause extends BattleClause {
   private Class[] abilities;

   public AbilityClause(String id, Class... abilities) {
      super(id);
      this.abilities = abilities;
      ArrayHelper.validateArrayNonNull(abilities);
   }

   public boolean validateSingle(Pokemon pokemon) {
      ItemHeld heldItem = pokemon.getHeldItemAsItemHeld();
      EnumSpecies species = pokemon.getBaseStats().getSpecies();
      if (PixelmonWrapper.canMegaEvolve(heldItem, species, pokemon.getForm())) {
         int form = false;
         int form;
         if (species != EnumSpecies.Necrozma) {
            form = ((ItemMegaStone)heldItem).getForm(pokemon.getForm());
         } else {
            form = EnumNecrozma.ULTRA.getForm();
         }

         BaseStats megaStats = species.getBaseStats(species.getFormEnum(form));
         AbilityBase megaAbility = (AbilityBase)AbilityBase.getAbility(megaStats.getAbilitiesArray()[0]).orElse((Object)null);
         Class megaAbilityClass = megaAbility == null ? null : megaAbility.getClass();
         if (ArrayHelper.contains(this.abilities, megaAbilityClass)) {
            return false;
         }
      }

      return !pokemon.getAbility().isAbility(this.abilities);
   }
}

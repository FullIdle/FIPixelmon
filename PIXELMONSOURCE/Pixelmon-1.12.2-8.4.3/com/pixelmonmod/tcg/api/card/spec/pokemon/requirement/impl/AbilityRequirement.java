package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AbilityRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"ability", "ab"});
   private AbilityBase ability;

   public AbilityRequirement() {
      super(KEYS);
   }

   public AbilityRequirement(AbilityBase ability) {
      this();
      this.ability = ability;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         if (args.length != 0 && args.length != 1) {
            AbilityBase ability = (AbilityBase)AbilityBase.getAbility(args[1]).orElse((Object)null);
            return ability == null ? Collections.emptyList() : Collections.singletonList(this.createInstance(ability));
         } else {
            return Collections.emptyList();
         }
      }
   }

   public Requirement createInstance(AbilityBase value) {
      return new AbilityRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return Objects.equals(this.ability, pokemon.getAbility());
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setAbility(this.ability.getNewInstance());
   }

   public AbilityBase getValue() {
      return this.ability;
   }
}

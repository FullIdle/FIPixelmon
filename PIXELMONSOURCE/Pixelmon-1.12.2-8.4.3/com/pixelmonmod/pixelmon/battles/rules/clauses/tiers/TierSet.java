package com.pixelmonmod.pixelmon.battles.rules.clauses.tiers;

import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonForm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.client.resources.I18n;

public abstract class TierSet extends Tier {
   private Set pokemon;

   protected TierSet(String id, Set pokemon) {
      super(id);
      this.setPokemon(pokemon);
      this.condition = this.getCondition();
      if (this.condition == null) {
         throw new NullPointerException("Condition cannot be null.");
      }
   }

   public TierSet setPokemon(Set pokemon) {
      if (pokemon == null) {
         pokemon = new HashSet();
      }

      this.pokemon = (Set)pokemon;
      return this;
   }

   protected abstract Predicate getCondition();

   protected boolean isInSet(PokemonForm pokemonForm) {
      return this.pokemon.contains(pokemonForm);
   }

   public String getTierDescription() {
      if (this.pokemon.isEmpty()) {
         return I18n.func_135052_a("pixelmon.command.tier.everything", new Object[0]);
      } else {
         List names = new ArrayList();
         Iterator var2 = this.pokemon.iterator();

         while(var2.hasNext()) {
            PokemonForm form = (PokemonForm)var2.next();
            names.add(form.getLocalizedName());
         }

         Collections.sort(names);
         StringBuilder builder = new StringBuilder();
         boolean first = true;
         Iterator var4 = names.iterator();

         while(var4.hasNext()) {
            String name = (String)var4.next();
            if (!first) {
               builder.append(", ");
            }

            first = false;
            builder.append(name);
         }

         return builder.toString();
      }
   }
}

package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class CanMegaEvolveRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"canmegaevo", "canmega", "canmegaevolve"});

   public CanMegaEvolveRequirement() {
      super(KEYS);
   }

   public CanMegaEvolveRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new CanMegaEvolveRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      if (pokemon.getSpecies() == EnumSpecies.Rayquaza) {
         return pokemon.getMoveset().hasAttack("Dragon Ascent");
      } else if (pokemon.getSpecies() != EnumSpecies.Greninja) {
         if (pokemon.getHeldItemAsItemHeld() == NoItem.noItem) {
            return false;
         } else if (pokemon.getHeldItemAsItemHeld().getHeldItemType() == EnumHeldItems.megaStone) {
            return ((ItemMegaStone)pokemon.getHeldItemAsItemHeld()).pokemon == pokemon.getSpecies();
         } else {
            return false;
         }
      } else {
         return pokemon.getFormEnum() == EnumGreninja.BATTLE_BOND || pokemon.getFormEnum() == EnumGreninja.ZOMBIE_BATTLE_BOND || pokemon.getFormEnum() == EnumGreninja.ALTER_BATTLE_BOND;
      }
   }

   public void applyData(Pokemon pokemon) {
   }
}

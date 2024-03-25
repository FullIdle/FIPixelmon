package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumAegislash;

public class StanceChange extends AbilityBase {
   public void preProcessAttackUser(PixelmonWrapper pokemon, PixelmonWrapper target, Attack a) {
      if (pokemon.getSpecies() == EnumSpecies.Aegislash) {
         if (Attack.dealsDamage(a) && pokemon.getForm() == EnumAegislash.SHIELD.getForm()) {
            pokemon.setForm(EnumAegislash.BLADE);
            pokemon.bc.modifyStats(pokemon);
            pokemon.bc.sendToAll("pixelmon.abilities.stancechange.blade", pokemon.getNickname());
         } else if (Attack.dealsDamage(a) && pokemon.getForm() == EnumAegislash.SHIELD_ALTER.getForm()) {
            pokemon.setForm(EnumAegislash.BLADE_ALTER);
            pokemon.bc.modifyStats(pokemon);
            pokemon.bc.sendToAll("pixelmon.abilities.stancechange.blade", pokemon.getNickname());
         }
      }

   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
      if (oldPokemon.getSpecies() == EnumSpecies.Aegislash && oldPokemon.getForm() == EnumAegislash.BLADE.getForm()) {
         oldPokemon.setForm(EnumAegislash.SHIELD);
      }

      if (oldPokemon.getSpecies() == EnumSpecies.Aegislash && oldPokemon.getForm() == EnumAegislash.BLADE_ALTER.getForm()) {
         oldPokemon.setForm(EnumAegislash.SHIELD_ALTER);
      }

   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumDarmanitan;

public class ZenMode extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.getSpecies() == EnumSpecies.Darmanitan && !pokemon.isFainted()) {
         EnumDarmanitan forme = (EnumDarmanitan)pokemon.getFormEnum();
         if (pokemon.getHealthPercent() < 50.0F && forme.isDefaultForm()) {
            pokemon.setForm(forme.getZenFromStandard());
            pokemon.bc.sendToAll("pixelmon.abilities.zenmode.zen", pokemon.getNickname());
         } else if (pokemon.getHealthPercent() >= 50.0F && pokemon.getFormEnum().isTemporary()) {
            pokemon.setForm(forme.getDefaultFromTemporary(pokemon.pokemon));
            pokemon.bc.sendToAll("pixelmon.abilities.zenmode.standard", pokemon.getNickname());
         }

      }
   }

   public void onAbilityLost(PixelmonWrapper pokemon) {
      if (pokemon.getSpecies() == EnumSpecies.Darmanitan && pokemon.getFormEnum().isTemporary()) {
         pokemon.setForm(pokemon.getFormEnum().getDefaultFromTemporary(pokemon.pokemon));
      }

   }
}

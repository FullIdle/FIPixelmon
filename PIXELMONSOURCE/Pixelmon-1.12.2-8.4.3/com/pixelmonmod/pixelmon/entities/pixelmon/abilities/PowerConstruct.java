package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumZygarde;

public class PowerConstruct extends AbilityBase {
   public void applyRepeatedEffectAfterStatus(PixelmonWrapper pokemon) {
      if (!pokemon.bc.simulateMode && pokemon.getSpecies() == EnumSpecies.Zygarde && pokemon.getFormEnum() != EnumZygarde.COMPLETE && pokemon.getHealthPercent() <= 50.0F) {
         pokemon.bc.sendToAll("pixelmon.abilities.powerconstruct.activate");
         pokemon.pokemon.getPersistentData().func_74768_a("SrcForm", pokemon.getForm());
         pokemon.evolution = new EvolutionQuery(pokemon.entity, EnumZygarde.COMPLETE.getForm());
         pokemon.setForm(EnumZygarde.COMPLETE);
         pokemon.bc.updateFormChange(pokemon.entity);
         pokemon.bc.sendToAll("pixelmon.abilities.powerconstruct.transform", pokemon.getNickname());
      }

   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sandstorm;

public class SandStream extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (!(newPokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Sandstorm) && newPokemon.bc.globalStatusController.canWeatherChange(new Sandstorm())) {
         Sandstorm sandstorm = new Sandstorm();
         sandstorm.setStartTurns(newPokemon);
         newPokemon.bc.globalStatusController.addGlobalStatus(sandstorm);
         newPokemon.bc.sendToAll("pixelmon.abilities.sandstream", newPokemon.getNickname());
      }

   }
}

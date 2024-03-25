package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Hail;

public class SnowWarning extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (!(newPokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Hail) && newPokemon.bc.globalStatusController.canWeatherChange(new Hail())) {
         Hail hail = new Hail();
         hail.setStartTurns(newPokemon);
         newPokemon.bc.globalStatusController.addGlobalStatus(hail);
         newPokemon.bc.sendToAll("pixelmon.abilities.snowwarning", newPokemon.getNickname());
      }

   }
}

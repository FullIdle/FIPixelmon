package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Rainy;

public class Drizzle extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (!(newPokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Rainy) && newPokemon.bc.globalStatusController.canWeatherChange(new Rainy())) {
         Rainy rainy = new Rainy();
         rainy.setStartTurns(newPokemon);
         newPokemon.bc.globalStatusController.addGlobalStatus(rainy);
         newPokemon.bc.sendToAll("pixelmon.abilities.drizzle", newPokemon.getNickname());
      }
   }
}

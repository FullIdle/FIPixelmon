package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Sunny;

public class Drought extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (!(newPokemon.bc.globalStatusController.getWeatherIgnoreAbility() instanceof Sunny) && newPokemon.bc.globalStatusController.canWeatherChange(new Sunny())) {
         Sunny sunny = new Sunny();
         sunny.setStartTurns(newPokemon);
         newPokemon.bc.globalStatusController.addGlobalStatus(sunny);
         newPokemon.bc.sendToAll("pixelmon.abilities.drought", newPokemon.getNickname());
      }
   }
}

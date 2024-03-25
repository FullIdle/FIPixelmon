package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Rainy;

public class Hydration extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.bc.globalStatusController.getWeather() instanceof Rainy && pokemon.hasPrimaryStatus(false)) {
         this.sendActivatedMessage(pokemon);
         pokemon.removePrimaryStatus();
      }

   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Rainy;

public class RainDish extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.bc.globalStatusController.getWeather() instanceof Rainy && !pokemon.hasFullHealth()) {
         pokemon.bc.sendToAll("pixelmon.abilities.raindish", pokemon.getNickname());
         pokemon.healByPercent(6.25F);
      }

   }
}

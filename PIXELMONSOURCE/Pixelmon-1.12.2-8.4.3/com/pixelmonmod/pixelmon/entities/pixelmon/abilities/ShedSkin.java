package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class ShedSkin extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (RandomHelper.getRandomChance(30) && pokemon.hasPrimaryStatus(false)) {
         this.sendActivatedMessage(pokemon);
         pokemon.removePrimaryStatus();
      }

   }
}

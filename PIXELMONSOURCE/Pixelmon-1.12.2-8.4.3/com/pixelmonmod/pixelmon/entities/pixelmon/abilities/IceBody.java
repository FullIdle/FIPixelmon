package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Hail;

public class IceBody extends AbilityBase {
   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      if (pokemon.getHealth() < pokemon.getMaxHealth() && pokemon.bc.globalStatusController.getWeather() instanceof Hail) {
         int par1 = (int)((float)pokemon.getMaxHealth() * 0.0625F);
         pokemon.healEntityBy(par1);
         pokemon.bc.sendToAll("pixelmon.abilities.icebody", pokemon.getNickname());
      }

   }
}

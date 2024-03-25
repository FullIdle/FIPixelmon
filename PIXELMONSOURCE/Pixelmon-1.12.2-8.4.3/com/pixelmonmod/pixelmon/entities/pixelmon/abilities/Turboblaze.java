package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Turboblaze extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      newPokemon.bc.sendToAll("pixelmon.abilities.turboblaze", newPokemon.getNickname());
   }
}

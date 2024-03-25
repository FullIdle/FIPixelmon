package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Pressure extends AbilityBase {
   public void preProcessAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.pp > 1 && !user.bc.simulateMode && !user.inMultipleHit) {
         --a.pp;
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      newPokemon.bc.sendToAll("pixelmon.abilities.pressure", newPokemon.getNickname());
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Soundproof extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.isSoundBased() && !a.isAttack("Heal Bell")) {
         pokemon.bc.sendToAll("pixelmon.abilities.soundproof", pokemon.getNickname());
         return false;
      } else {
         return true;
      }
   }
}

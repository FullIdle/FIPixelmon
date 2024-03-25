package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Overcoat extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (isPowderMove(a)) {
         pokemon.bc.sendToAll("pixelmon.abilities.overcoat", pokemon.getNickname());
         return false;
      } else {
         return true;
      }
   }

   public static boolean isPowderMove(Attack a) {
      return a != null && a.isAttack("Cotton Spore", "Poison Powder", "Powder", "Rage Powder", "Sleep Powder", "Spore", "Stun Spore");
   }
}

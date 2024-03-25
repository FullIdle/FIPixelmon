package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.MagicCoat;

public class MagicBounce extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (AbilityBase.ignoreAbility(user, pokemon)) {
         return true;
      } else {
         return !MagicCoat.reflectMove(a, pokemon, user, "pixelmon.abilities.magicbounce");
      }
   }
}

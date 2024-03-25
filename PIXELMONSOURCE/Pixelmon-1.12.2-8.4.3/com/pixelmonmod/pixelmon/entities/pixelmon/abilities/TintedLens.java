package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class TintedLens extends AbilityBase {
   public int modifyDamageUser(int damage, PixelmonWrapper user, PixelmonWrapper pokemon, Attack a) {
      double effectiveness = a.getTypeEffectiveness(user, pokemon);
      if (effectiveness == 0.5 || effectiveness == 0.25) {
         damage *= 2;
      }

      return damage;
   }
}

package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Filter extends AbilityBase {
   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper pokemon, Attack a) {
      double effectiveness = a.getTypeEffectiveness(user, pokemon);
      if (effectiveness == 2.0 || effectiveness == 4.0) {
         damage = (int)((double)damage * 0.75);
      }

      return damage;
   }
}

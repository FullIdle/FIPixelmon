package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Multiscale extends AbilityBase {
   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper pokemon, Attack a) {
      return pokemon.getHealth() == pokemon.getMaxHealth() ? damage / 2 : damage;
   }
}

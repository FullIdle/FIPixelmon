package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class ThickFat extends AbilityBase {
   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper pokemon, Attack a) {
      if (a.getType() == EnumType.Fire || a.getType() == EnumType.Ice) {
         damage /= 2;
      }

      return damage;
   }
}

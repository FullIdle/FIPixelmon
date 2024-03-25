package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Fluffy extends AbilityBase {
   public int modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getType() == EnumType.Fire) {
         damage *= 2;
      }

      if (a.getMove().getMakesContact()) {
         damage /= 2;
      }

      return damage;
   }
}
